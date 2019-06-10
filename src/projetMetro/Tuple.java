package projetMetro;

public class Tuple<S1, S2> {
	private S1 e1;
	private S2 e2;
	public Tuple(S1 obj,S2 obj2) {
		this.setE1(obj);
		this.setE2(obj2);
	}
	public S2 getE2() {
		return e2;
	}
	public void setE2(S2 e2) {
		this.e2 = e2;
	}
	public S1 getE1() {
		return e1;
	}
	public void setE1(S1 e1) {
		this.e1 = e1;
	}
}
