package sample.Carriers;

import java.util.Scanner;

public class Aircraft extends Carrier {

	private int dlina;
	// зачем вам методы гет/сет длина, которые не используются?


	@Override
	void setPrice(int param) {
		this.price = param*10;
	}


	public Aircraft(String town1, String town2){
		this.speed=setParam("speed");
		this.dlina=setParam("dlina");
		setPrice(dlina);
		this.name="�������";

		this.town1 = town1;
		this.town2 = town2;
	}


	@Override
	public String showAnswer(int length) {
		return  "�������: " + name + "\n" +
				"����� ��������: " + dlina + "\n" +
				"�������� ����������� "+this.name+
				" ������ "+String.valueOf(String.format("%.2f", time(length)))+
				" ����� � ����� ������ "+String.valueOf(totalPrice(length))+" ������";
	}

	public void makeNiceHooverSound() {
		System.out.println("WHOOOOOOOOOOOSHHHH!!");
	}
}
