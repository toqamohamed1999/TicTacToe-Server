
package Logic;


public class User {
    
    int ID;
    String userName;
    String email;
    String password;
    String gender;
    int score;
    String recordPath;
    String IP;
    
    public User(){}

    public User(int ID, String userName, String email, String password, String gender, int score, String recordPath) {
        this.ID = ID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.score = score;
        this.recordPath = recordPath;
    }
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "User{" + "ID=" + ID + ", userName=" + userName + ", email=" + email + ", password=" + password + ", gender=" + gender + ", score=" + score + ", recordPath=" + recordPath + ", IP=" + IP + '}';
    }

    
}
   
