package br.com.pix.qware.qwcfp.teste;

public class Calc {

	private static String	muitoAtivo		= "Muito Ativo";

	private static String	ativo			= "Ativo";

	private static String	irregularAtivo	= "Irregularmente Ativo";

	private static String	sedentario		= "Sedentario";

	public String classNivelAtividadeFisica(Integer diasCaminhados,
			Integer diasCaminhadosTempo,
			Integer diasModerados,
			Integer diasModeradosTempo,
			Integer diasVigorosos,
			Integer diasVigorosoTempo) {
		
		if(diasCaminhados == null && diasCaminhadosTempo == null && diasModerados == null && diasModeradosTempo == null && diasVigorosos == null & diasVigorosoTempo == null)
			return null;
		
		Integer somaCaminhadaModerada = diasCaminhados + diasModerados;
		Integer somaCaminhadaModeradaTempo = diasCaminhadosTempo + diasModeradosTempo;
		Integer somaTodasAtividadas = diasCaminhados + diasModerados + diasVigorosos;
		Integer somaTodasAtividadasTempo = diasCaminhadosTempo + diasModeradosTempo + diasVigorosoTempo;
		
		if(diasVigorosos >= 5 && diasVigorosoTempo >= 30)
			return muitoAtivo;
		else if(diasVigorosos >= 3 && diasVigorosoTempo >= 20  && somaCaminhadaModerada >= 5 && somaCaminhadaModeradaTempo >=30)
			return muitoAtivo;
		else if(diasVigorosos >= 3 && diasVigorosoTempo >= 20)
			return ativo;
		else if(somaCaminhadaModerada >= 5 && somaCaminhadaModeradaTempo >= 30)
			return ativo;
		else if(somaTodasAtividadas >= 5 && somaTodasAtividadasTempo >= 150)
			return ativo;
		else if(somaTodasAtividadas == 0 && somaTodasAtividadasTempo == 0 )
			return sedentario;
		else
			return irregularAtivo;
			
	}

	public static void main(String[] args) {

		String a = "Moderada (5 mets)";
		String[] tokens = a.split("\\(");
		String b = tokens[0].trim();
		System.out.println(b);
	}
}
