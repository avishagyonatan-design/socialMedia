import java.time.LocalDateTime;

public class EventPost extends Post {

    private LocalDateTime eventDateTime;
    private String location;
    /**
     * בנאי היוצר מופע חדש של פוסט מסוג אירוע.
     * הפעולה מאתחלת את פרטי הפוסט הבסיסיים דרך מחלקת האב, ומוסיפה מיקום ומועד לאירוע.
     *
     * @param author        המשתמש (User) שכתב ויצר את פוסט האירוע.
     * @param content       תוכן הפוסט (תיאור האירוע).
     * @param eventDateTime מועד ושעת קיום האירוע.
     * @param location      המיקום הפיזי שבו יתקיים האירוע.
     * @throws EmptyContentException אם תוכן הפוסט ריק או מכיל רווחים בלבד.
     */
    public EventPost(User author, String content, LocalDateTime eventDateTime,  String location) throws EmptyContentException{
        super(author, content);
        this.eventDateTime = eventDateTime;
        this.location = location;
    }
    /**
     * מחזירה את מועד ושעת האירוע המתוכנן.
     * @return  אובייקט LocalDateTime המייצג את זמן האירוע.
     */
    public LocalDateTime getEventDateTime(){
        return eventDateTime;
    }
    /**
     * מחזירה את המיקום שבו מתקיים האירוע.
     * @return מחרוזת (String) המייצגת את מיקום האירוע.
     */
    public String getLocation(){
        return location;
    }

    @Override
    public String toString() {
        return "post #" + this.getId() + " | @" +this.getAuthor().getUserName() + " | " + this.getContent() +" | " + location + " | " + eventDateTime + " | Likes: " + this.getLikesCount() + " | " + this.eventDateTime.toString().replace("T", " ");
    }
}
