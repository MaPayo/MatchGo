package es.ucm.fdi.iw.model;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.ucm.fdi.iw.model.User.Role;


/**
 * We only test the methods that have more logic than get and set in User
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = User.class)
public class UserTest {
    private User user;

    @Test
    public void hasRoleTest(){
        user = new User((long) 1,"nm", "n", "m", "n@m", "aa", "2003-07-09", "male", "USER");
        Assert.assertTrue(user.hasRole(Role.USER));
    }

    @Test
    public void encodePasswordTest(){
        user = new User((long) 1,"nm", "n", "m", "n@m", "aa", "2003-07-09", "male", "USER");
        //Comprobamos que se añade bien la sal haciendo que los dos encodes sean distintos
        Assert.assertFalse(User.encodePassword(user.getPassword()) == User.encodePassword(user.getPassword()));
    }


    @Test
    public void passwordMatches(){
        user = new User((long) 1,"nm", "n", "m", "n@m", "aa", "2003-07-09", "male", "USER");
        String encode = User.encodePassword(user.getPassword());
        user.setPassword(encode);
        Assert.assertTrue(user.passwordMatches("aa"));
    }





    
}