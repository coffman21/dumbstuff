import sample.Carriers.*;

import java.util.*;
public class Main {

public static void main(String[] args)

{
	Scanner in = new Scanner(System.in);

	System.out.print("������� ��������� �����: ");
	String town1=in.nextLine();
	System.out.print("������� �������� �����: ");
	String town2=in.nextLine();
	System.out.print("������� ����������: ");
	int length=in.nextInt();
	
	Carrier arr[] = new Carrier[3];
	arr[0] = new Car(town1, town2, 1000);
	arr[1] = new Aircraft(town1, town2);
	arr[2] = new Train(town1, town2);

	for (Carrier anArr : arr) {
		System.out.println(anArr.showAnswer(length));
	}

}
}

