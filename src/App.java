/**
 * מחלקה הראשית המכילה את מתודת MAIN
 */
import java.time.LocalDateTime;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {

        // 1. יצירת רשת חברתית בשם SocialSim
        SocialNetwork socialSim = new SocialNetwork("SocialSim");

        // אנחנו מגדירים את המשתנים כאן למעלה כדי שנוכל לגשת אליהם מכל הבלוקים בהמשך
        User nor = new User("Nor", "Hello!");
        User dan = new User("Dan", "Hi there!");
        User ron = new User("Ron", "I love coding!");

        Post post1 = null;
        EventPost post2 = null;
        PollPost post3 = null;

        try {
            // 2. יצירת והוספת המשתמשים למערכת
            socialSim.addUser(nor);
            socialSim.addUser(dan);
            socialSim.addUser(ron);

            // 3. יצירת סוגי הפוסטים השונים
            // פוסט טקסט
            post1 = socialSim.createPost(nor, "Just joined SocialSim!");

            // פוסט אירוע
            LocalDateTime eventDate = LocalDateTime.of(2026, 6, 10, 18, 0);
            post2 = socialSim.createEventPost(dan, "Hello everyone!", eventDate, "Tel Aviv");

            // פוסט סקר
            ArrayList<String> options = new ArrayList<>();
            options.add("Yes");
            options.add("No");
            options.add("Maybe");
            post3 = socialSim.createPollPost(ron, "Java is awesome!", options);

            // 4. הוספת תגובות
            socialSim.addComment(dan, post1, "Welcome Nor!");
            socialSim.addComment(ron, post1, "Great to have you here!");
            socialSim.addComment(nor, post2, "Nice to meet everyone!");

            // 5. הוספת לייקים
            socialSim.likePost(post1, dan);
            socialSim.likePost(post1, ron);
            socialSim.likePost(post2, nor);

        } catch (ApplicationException e) {
            System.out.println("Error during setup: " + e.getMessage());
        }




        // ==========================================
        // 7. הדפסות (כפי שנדרש בעמוד האחרון)
        // ==========================================

        System.out.println("\nUSERS");
        // נניח שיש לך מתודה שמחזירה את רשימת המשתמשים מתוך הרשת
        for (User u : socialSim.getUsers()) {
            System.out.println(u.toString());
        }

        System.out.println("\nPOSTS");
        for (Post p : socialSim.getPosts()) {
            System.out.println(p.toString());
        }

        System.out.println("\nCOMMENTS FOR POST #1");
        for (Comment c : post1.getComments()) {
            System.out.println(c.toString());
        }

        System.out.println("\nCOMMENTS FOR POST #2");
        for (Comment c : post2.getComments()) {
            System.out.println(c.toString());
        }

        System.out.println("\nCOMMENTS FOR POST #3");
        if (post3.getComments().isEmpty()) {
            System.out.println("No comments");
        } else {
            for (Comment c : post3.getComments()) {
                System.out.println(c.toString());
            }
        }

        System.out.println("\nNOTIFICATIONS FOR Nor");
        for (Notification n : nor.getNotifications()) {
            System.out.println(n.toString());

        }

            // 6. הדגמת חריגות בבלוקים נפרדים
        System.out.println();
        System.out.println("EXCEPTIONS:");

        System.out.println();


            // יצירת פוסט עם תוכן ריק
            try {
                socialSim.createPost(nor, "");
            } catch (ApplicationException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            // יצירת פוסט סקר עם אפשרות בחירה אחת
            try {
                ArrayList<String> badOptions = new ArrayList<>();
                badOptions.add("Yes");
                socialSim.createPollPost(ron, "Test", badOptions);
            } catch (ApplicationException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            // חיפוש משתמש שאינו קיים
            try {
                socialSim.findUserByUsername("Moshe");
            } catch (ApplicationException e) {
                // המרצה ביקשה פלט ספציפי פה
                System.out.println("ERROR: User Moshe not found");
            }

            // לייק כפול לאותו פוסט
            try {
                // דן כבר עשה לייק לפוסט 1 בשלב 5, אז הניסיון הזה יזרוק שגיאה
                socialSim.likePost(post1, dan);
            } catch (ApplicationException e) {
                // פלט ספציפי לפי ההנחיות
                System.out.println("ERROR: Dan already liked this post");
            }
    }
}
