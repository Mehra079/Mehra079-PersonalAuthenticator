//package com.bootcamp.backend.util;
//
//import org.mindrot.jbcrypt.BCrypt;
//
//public class PasswordEncoderUtil {
//    public static String hashPassword(String plainPassword) {
//        // BCrypt provides a salt automatically
//        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
//    }
//    public static boolean checkPassword(String plainPassword, String hashedPassword) {
//        return BCrypt.checkpw(plainPassword, hashedPassword);
//    }
//}
