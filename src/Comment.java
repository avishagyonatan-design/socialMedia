import java.time.LocalDateTime;

/**
 * מחלקה המייצגת תגובה על פוסט
 * מכילה את תוכן התגובה, מחבר התגובה והפוסט שאליו משוייכת
 */
public class Comment {


    private static int counter = 1;

    private int id;
    private User author;
    private Post post;
    private String content;
    private LocalDateTime timestamp;

    public Comment(User author, Post post , String content){
        this.id = counter++;//מקצה מספר מזהה ID
        this.timestamp = LocalDateTime.now();//אתחול תאריך הפרסום
        this.author = author;
        this.post = post;
        this.content = content;
    }

    /**
     * מעדכנת את מספר המזהה של התגובה
     * @param id המספר המזהה החדש שיוקצה לתגובה
     */
    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }
    public Post getPost(){
        return post;
    }
    public String getContent(){
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String toString(){
        int day = timestamp.getDayOfMonth();
        int month = timestamp.getMonthValue();
        int year = timestamp.getYear();
        int hour = timestamp.getHour();
        int minute = timestamp.getMinute();

        //טיפול בדקות ובשעות שיוצגו יפה
        String minuteStr = " ";
        if (minute < 10 ){
            minuteStr = "0" + minute;
        }else {
            minuteStr = " " + minute;
        }
        String hourStr = " ";
        if (hour < 10 ){
            hourStr = "0" + hour;
        }else {
            hourStr = " " + hour;
        }
        //נבנה תאריך ושעה
        String dateTime = day + "." + month + "." + year + " " + hourStr + " : " + minuteStr;

        // מחזירים את המחרוזת עם פרטי פוסט לפי הפורמט
        return "comment #" + id + " | @" + author.getUserName() + " | " + content + " | "  + dateTime;
    }
}
