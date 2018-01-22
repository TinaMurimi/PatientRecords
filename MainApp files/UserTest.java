package patientrecords;

import java.util.HashMap;

import patientrecords.models.User;

public class UserTest {
    public static void main(String[] args) {
        String username = "Tina";

        User user, user2;

        // System.out.println(user.getUsername());

        user = User.of(username);
        user2 = User.of("leah");
        user2.setPassword("123456");

        User userDetails = user.getUserDetails();
        User user2Details = user2.getUserDetails();


        System.out.println("\n---------User details---------");
        System.out.println(userDetails.getUsername());
        System.out.println();
        System.out.println(user2Details.getUsername());
    }
}
