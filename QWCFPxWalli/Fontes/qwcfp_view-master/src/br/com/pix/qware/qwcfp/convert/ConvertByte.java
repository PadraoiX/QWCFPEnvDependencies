package br.com.pix.qware.qwcfp.convert;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("convertByte")
public class ConvertByte implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		final long K = 1024;
		final long M = K * K;
		final long G = M * K;
		final long T = G * K;

		final long[] dividers = { T, G, M, K, 1 };
		final String[] units = { "TB", "GB", "MB", "KB", "B" };
		String retorno = null;
		Long valor = Long.parseLong(arg2);
		if (valor < 1)
			throw new IllegalArgumentException("Invalid file size: " + valor);
		for (int i = 0; i < dividers.length; i++) {
			final long divider = dividers[i];
			if (valor >= divider) {
				double result = divider > 1 ? (double) valor / (double) divider
						: (double) valor;
				retorno = new DecimalFormat("#,##0.#").format(result) + " "
						+ units[i];
				break;
			}
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return arg2.toString();
	}

}
