/**
 * מחלקה המייצגת התראה שנישלחה למשתמש ברשת חברתית
 * שומרת את השולח, המקבל, תוכן ההודעה, חותמת הזמן וסטטוס הקריאה
 */

import java.time.LocalDateTime;

public class Notification {

    private static int counter = 1;

    private int id;
    private User sender;
    private User recipient;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Notification(User sender, User recipient, String content){
        this.id = counter++;
        this.timestamp = LocalDateTime.now();
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }
    public User getRecipient(){
        return recipient;
    }
    public String getContent(){
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
//אם קרא את ההודעה המתודה תחזיר TRUE אחרת FALSE
    public boolean isRead() {
        return isRead;
    }
    public void markAsRead(){
        this.isRead = true;//מסמנת את ההתראה כניקראה
    }
    public String toString(){
        int day = timestamp.getDayOfMonth();
        int month = timestamp.getMonthValue();
        int year = timestamp.getYear();
        int hour = timestamp.getHour();
        int minute = timestamp.getMinute();

        //סידור שעות ,דקות,חודש,יום יפה
        String minuteStr = (minute < 10) ? "0" + minute : " " + minute;
        String hourStr = (hour < 10) ? "0" + hour : " " + hour;
        String monthStr = (month < 10) ? "0" + month : " " + month;
        String dayStr = (day < 10) ? "0" + day : " " + day;
        //נבנה את התאריך והשעה
        String dateTime = dayStr + "." + monthStr + "." + year + " " + hourStr + ":" + minuteStr;

        //ננדוק האם ההתראה ניקראה
        String readStatus = this.isRead ? "read" : "unread";
        return "Notification #" + this.id + " | to: @" + this.recipient.getUserName() + " | from: @" + this.sender.getUserName() + " | " + this.content +" | " + readStatus +" | " + dateTime;
    }

}
