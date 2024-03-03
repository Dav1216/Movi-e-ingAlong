// package com.movingalong;

// import dev.morphia.Datastore;

// import org.bson.Document;
// import org.bson.types.ObjectId;

// import com.movingalong.entities.User;
// import com.movingalong.utils.UtilsDB;

// public class Main {
//     public static void main(String[] args) {
//         UtilsDB utilsDB = new UtilsDB();
//         Datastore datastore = utilsDB.getDatastore();
//         // datastore.save(user);
//         Document query = new Document("_id", new ObjectId("65e24d96046d5006c0ff5c37")); 
//         User user2 = datastore.find(User.class, query).first();
//         // System.out.println(user2.toString());
//         // // datastore.delete(user2);
//         // System.out.println(user2);
//         // List<User> userList = datastore.find(User.class).iterator().toList();
//         // userList.stream().forEach(System.out::println);
//         datastore.delete(user2);
//         // user2.setEmail("john@hagoo.com");
//         // datastore.insert(user2);
//         utilsDB.closeClient();
//     }

//     private static Object ObjectId(String string) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'ObjectId'");
//     }
// }