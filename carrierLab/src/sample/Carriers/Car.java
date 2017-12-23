package sample.Carriers;

import java.util.Random;
import java.util.Scanner;

public class Car extends Carrier {
	private int greed;
	private int capacity;

	@Override
	void setPrice(int param) {
		this.price = param*0.5;
	}

	public Car(String town1, String town2, int capacity) {
		this.speed = setParam("speed");
		this.greed = setParam("greed");
		setPrice(greed);
		this.name ="����������";
		this.capacity = capacity;

		this.town1 = town1;
		this.town2 = town2;
	}

	@Override
	public String showAnswer(int length) {
		return  "����������: " + name + "\n" +
				"������ �������: " + greed + "\n" +
				"�������� ����������� "+ this.name+
				" ������ "+String.valueOf(String.format("%.2f", time(length)))+
				" ����� � ����� ������ "+String.valueOf(totalPrice(length))+" ������";

	}

	public void isAvailableToTransportPiano() {
		if (this.capacity >= 1000) {
			System.out.println("Yes, it can transport piano");
		} else {
			System.out.println("No, it can not transport piano");
		}
	}
}