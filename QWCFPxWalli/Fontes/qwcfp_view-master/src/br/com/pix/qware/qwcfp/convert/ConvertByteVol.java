package br.com.pix.qware.qwcfp.convert;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.beans.QwcssVolumeInformationBean;

@FacesConverter("convertByteVol")
public class ConvertByteVol implements Converter {
	
	
	private final long K = 1024;
	private final long M = K * K;
	private final long G = M * K;
	private final long T = G * K;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		QwcssVolumeInformationBean bean = context.getApplication().evaluateExpressionGet(context, "#{volumeBean}", QwcssVolumeInformationBean.class) ;
		if (bean != null) {
			final long[] dividers = { T, G, M, K, 1 };
			final String[] units = { "TB", "GB", "MB", "KB", "B" };
			Long valor = Long.parseLong(arg2);
			if (valor < 1)
				throw new IllegalArgumentException("Invalid file size: " + valor);
			for (int i = 0; i < dividers.length; i++) {
				final long divider = dividers[i];
				if (valor >= divider) {
					double result = divider > 1 ? (double) valor / (double) divider
							: (double) valor;
					return new BigDecimal(result);
				}
			}
		}
		
		return new BigDecimal(0L);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		QwcssVolumeInformationBean bean = context.getApplication().evaluateExpressionGet(context, "#{volumeBean}", QwcssVolumeInformationBean.class) ;
		if (bean != null) {
			if(arg2 != null){
				Long bte = null;
				try {
					
					bte = ((BigDecimal) arg2).longValue();
				} catch (Exception e) {
					bte = 0L;
				}
	
				if ((bte >= 0) && (bte < K)) {
					bean.setSizeVol("1");
					return Long.toString(bte);
	
				} else if ((bte >= K) && (bte < M)) {
					bean.setSizeVol("1024");
					return Long.toString(bte / K);
	
				} else if ((bte >= M) && (bte < G)) {
					bean.setSizeVol("1048576");
					return Long.toString(bte / M);
	
				} else if ((bte >= G) && (bte < T)) {
					bean.setSizeVol("1073741824");
					return Long.toString(bte / G);
	
				} else if (bte >= T) {
					bean.setSizeVol("1099511627776");
					return Long.toString(bte / T);
				} else {
					return Long.toString(bte);
				}
			}
		}
		return "";
	}

}
