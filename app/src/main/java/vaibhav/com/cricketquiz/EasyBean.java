package vaibhav.com.cricketquiz;

import java.io.Serializable;

public class EasyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type, text, image, answer;
	int solved;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSolved() {
		return solved;
	}

	public void setSolved(int solved) {
		this.solved = solved;
	}

}
