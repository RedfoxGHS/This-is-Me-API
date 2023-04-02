package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.enums.RoleName;
import br.inatel.thisismeapi.exceptions.OnCreateDataException;
import br.inatel.thisismeapi.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.exceptions.UnregisteredUserException;
import br.inatel.thisismeapi.exceptions.mongo.UniqueViolationConstraintException;
import br.inatel.thisismeapi.models.Roles;
import br.inatel.thisismeapi.repositories.UserRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.MailService;
import br.inatel.thisismeapi.services.UserService;
import br.inatel.thisismeapi.utils.JwtUtils;
import br.inatel.thisismeapi.utils.RandomUtils;
import br.inatel.thisismeapi.utils.SendEmailUtils;
import br.inatel.thisismeapi.utils.UserUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final long RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS = 43200;

    @Value("${private.key.default}")
    private String PRIVATE_KEY_DEFAULT;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private MailService mailService;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public String saveNewAccount(String email, String password, String verifyPassword, String characterName) {

        LOGGER.info("m=saveNewAccount, type=User, email={}, characterName={}", email, characterName);
        UserUtils.verifyEmail(email);
        email = email.toLowerCase();
        UserUtils.verifyPassword(password, verifyPassword);
        UserUtils.verifyDecryptedPasswordLength(password);

        this.validadeCharacterName(characterName);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder().encode(password));
        user.setRoles(this.getUserRoles());

        Character character = characterService.saveNewCharacter(new Character(email, characterName));
        user.setCharacter(character);

        try {
            userRepository.save(user);
            return JwtUtils.createJwtTokenLoginWith(user, this.PRIVATE_KEY_DEFAULT);
        } catch (DuplicateKeyException e) {
            throw new UniqueViolationConstraintException("Já Existe uma conta cadastrada com esse e-mail!");
        } catch (Exception e) {
            throw new OnCreateDataException(e.getLocalizedMessage());
        }
    }

    @Override
    public User updateUser(User user) {

        LOGGER.info("m=updateUser, type=User, email={}", user.getEmail());
        if (user.getId() == null)
            throw new UnregisteredUserException("Usuário não registrado!");
        return this.userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {

        LOGGER.info("m=findUserByEmail, type=User, email={}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty())
            throw new UnregisteredUserException("Usuário com email [" + email + "] não encontrado!");


        return userOptional.get();
    }

    @Override
    public void sendEmailToResetPassword(String email) {

        LOGGER.info("m=sendEmailToResetPassword, type=User, email={}", email);
        User user = this.findUserByEmail(email);

        Long number = RandomUtils.randomGenerate(100_000L, 999_999L);

        mailService.sendEmailWithMessage(
                user.getEmail(), SendEmailUtils.SUBJECT_RESET_TOKEN, SendEmailUtils.getMessageResetToken(number));

        String jwt = JwtUtils.createJwtResetTokenWith(
                user.getEmail(), number.intValue(), RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, this.PRIVATE_KEY_DEFAULT);

        user.setTokenResetPassword(jwt);
        this.updateUser(user);
    }


    @Override
    public String getResetTokenWithEmailAndNumber(String email, Integer number) {

        LOGGER.info("m=getResetTokenWithEmailAndNumber, type=User, email={}", email);
        User user = this.findUserByEmail(email);

        String resetToken = user.getTokenResetPassword();
        Integer tokenNumber = null;
        try {
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(resetToken, this.PRIVATE_KEY_DEFAULT);

            tokenNumber = decodedJWT.getClaim("number").asInt();

        } catch (JWTVerificationException | NullPointerException e) {
            throw new TokenInvalidException("Não foi gerado o código de verificação, por favor solicite outro código e tente novamente!");
        }

        if (!number.equals(tokenNumber)) {
            throw new TokenInvalidException("Código de verificação incorreto!");
        }

        LOGGER.info("m=getResetTokenWithEmailAndNumber, status=validated token");
        return resetToken;
    }

    @Override
    public void resetPassword(String password, String passwordVerify, String resetToken) {

        LOGGER.info("m=resetPassword");
        UserUtils.verifyPassword(password, passwordVerify);
        UserUtils.verifyDecryptedPasswordLength(password);
        String email;

        try {
            DecodedJWT decodedJWT = JwtUtils.decodedJWT(resetToken, this.PRIVATE_KEY_DEFAULT);
            email = decodedJWT.getClaim("email").asString();

        } catch (JWTVerificationException e) {
            LOGGER.error("m=resetPassword, errorMsg={}", e.getMessage());
            throw new TokenInvalidException("Não foi gerado o código de verificação, por favor solicite outro código e tente novamente!");
        }
        User user = this.findUserByEmail(email);

        if (user.getTokenResetPassword() == null) {
            throw new TokenInvalidException("Token já utilizado ou não foi gerado, por favor crie um novo código para a alteração de senha!");
        }

        user.setPassword(passwordEncoder().encode(password));
        user.setTokenResetPassword(null);
        this.updateUser(user);
        LOGGER.info("m=resetPassword, email={}, status=senha alterada com sucesso", email);

    }

    private List<Roles> getUserRoles() {
        List<Roles> roles = new ArrayList<>();
        roles.add(new Roles(RoleName.ROLE_USER));
        return roles;
    }

    private void validadeCharacterName(String characterName) {

        if (characterName == null || characterName.isEmpty())
            throw new IllegalArgumentException("Nome do personagem inválido!");

        if (characterName.length() > 15)
            throw new IllegalArgumentException("Nome do personagem pode ter no máximo 15 caracteres!");
    }
}
