package Smartcontactmanager.helper;

public class Messege {
private String content;
private String type;
public String getContent() {
	return content;
}
public Messege() {
	super();
	// TODO Auto-generated constructor stub
}
public void setContent(String content) {
	this.content = content;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public Messege(String content, String type) {
	super();
	this.content = content;
	this.type = type;
}

}
