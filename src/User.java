/**
 * מחלקה המייצגת משתמש ברשת החברתית
 * שומרת את הפרטי המשתמש, הפוסטים שכתבת ואת תיבת ההתראות שלו0
 */

import java.time.LocalDateTime;
import java.util.ArrayList;


public class User {
    private static int counter = 1 ;

    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();

    private int id;
    private String username;
    private String bio;
    private LocalDateTime joinDate;

    public User (String username,String bio){
        this.id= counter++;
        this.username = username;
        this.bio = bio;
        this.joinDate = LocalDateTime.now();
    }
    public int getId(){
        return id;//מחזיר ID
    }
    public String getUserName(){
        return username;
    }
    public void setBio(String bio){
        this.bio = bio;//מעדכן את הפרופיל
    }
    public String getBio(){
        return bio;
    }
    public LocalDateTime getJoinDate(){
        return joinDate;
    }
    public String toString(){
        //יצירת תבנית לתאריך שתתאים לדוגמא שהבאת(יום.שנה.חןדש)
        int day = joinDate.getDayOfMonth();
        int month = joinDate.getMonthValue();
        int year = joinDate.getYear();

        String date = day + "." + month + "." + year;
        return "User #" + id + " | @" +username + " | Bio: "+ bio +" | Joined: " + date;
    }
    public void addPost(Post post){
        this.posts.add(post);
    }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public int getPostCount(){
         return posts.size();
    }

    /**
     * מוסיפה התראה חדשה לרשימת ההתראות של המשתמש
     * @param notification ההתראה שהתקבלה
     */
    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * מחשבת ומחזירה את כמות ההתראות של המשתמש שטרם ניקראו
     * @return מספר ההתראות בסטטוס ANREAD
     */
    public int getUnreadNotificationCount(){
        //הגדרת משתנה עזר שמתחיל מ0
        int count = 0;
        //לולאת פור איצ שעוברתץ על כל התראה בתוך רשימת ההתראות
        for (Notification notification : this.notifications){
            if (notification.isRead() == false){//אנחנו בודקים אם ההתראה הספיציפית עדיין לא ניקראה
                count++;//מצאנו התראה שלא ניקראה לכן נוסיף אחד למונה
            }
        }
        return count;//מחזירים את המספר הסופי
    }

    /**
     * מדפיסה את כל ההתראות המשתמש
     */
    public void printNotifications(){
        if (this.notifications.isEmpty()){
            System.out.println("No Notification");
        }else {
            for (int i = 0; i < this.notifications.size(); i++){
                System.out.println(this.notifications.get(i));
            }
        }
    }
}
