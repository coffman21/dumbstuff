package sample.Carriers;

import java.util.Scanner;

public abstract class Carrier {

	String name;
	String town1;
	String town2;
	double price;
	int speed;

	private static Scanner in = new Scanner(System.in);

	double time(int length) {
		return this.speed * length;
	}

	double totalPrice(int length) {
		return this.price * length;
	}


	// вот тут бы использовать интерфейс
	void setPrice(int param) {
		this.price = 0;
	}

	static int setParam(String paramName) {
		System.out.println("Enter " + paramName + ":");
		return in.nextInt();
	}


	// не экономьте на переносах строк

	// зачем вам неиспользуемые параметры town1, town2?

	public String showAnswer(int length) {
		return "�������� ����������� "+this.name
				+" ������ "+String.valueOf(String.format("%.2f", time(length)))
				+" ����� � ����� ������ "+String.valueOf(totalPrice(length))+" ������";
	}

}
