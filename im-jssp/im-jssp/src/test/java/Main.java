import org.intra_mart.jssp.page.JSSPInitializer;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		String prop4home = System.getProperty("jssp.home");
		String prop4conf = System.getProperty("jssp.config");

		String home = (prop4home != null) ? prop4home : System.getProperty("user.dir");
		String conf = (prop4conf != null) ? prop4conf : "/conf/jssp-config-temp.xml";
				
		// 初期化
		// コンフィグファイル 「intra-mart/jssp/initializer/application/initializer-script」 を実行します。
		JSSPInitializer.init(home, conf);
	}
}
