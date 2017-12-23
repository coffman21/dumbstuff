
package sample.Carriers;

import java.util.Scanner;

public class Train extends Carrier {
	private int vagon;

	@Override
	void setPrice(int param) {
		this.price = param*0.05;
	}


	public Train(String town1, String town2){
		this.speed = setParam("speed");
		this.vagon = setParam("vagon");
		setPrice(vagon);
		this.name="�����";

		this.town1 = town1;
		this.town2 = town2;
	}
	@Override
	public String showAnswer(int length) {
		return "���������: " + name + "\n" +
				"���������� �������: " + vagon + "\n" +
				"�������� ����������� "+this.name+
				" ������ "+String.valueOf(String.format("%.2f", time(length)))+
				" ����� � ����� ������ "+String.valueOf(totalPrice(length))+" ������";

	}

	public void getLengthOfTrain() {
		System.out.println("As this train has " + vagon
				+ " cars in it, its total length is " + vagon*10
				+ " merets.");
	}
}
