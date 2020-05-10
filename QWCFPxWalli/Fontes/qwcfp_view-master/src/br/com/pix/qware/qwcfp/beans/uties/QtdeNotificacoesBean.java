package br.com.pix.qware.qwcfp.beans.uties;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.pix.qware.qwcfp.util.Util;
import br.com.qwcss.ws.dto.SimpleDTO;

@Named("qtdeNotify")
@RequestScoped
public class QtdeNotificacoesBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1883295340714432286L;

	@Inject
	private NotifyService notifyService;

	@Inject
	private LoginBean loginBean;

	private static final String NOTIFIE_ALL_MY_GROUP = "NOTIFIE_ALL_MY_GROUP";
	private Integer qtdNotifiesAllMyGroup;
	
	private static final String NOTIFIE_ALL_MY_AREAS = "NOTIFIE_ALL_MY_AREAS";
	private Integer qtdNotifiesAllMyAreas;

	private static final String NOTIFIE_PESSOAIS = "NOTIFIE_PESSOAIS";
	private Integer qtdNotifiesPessoais;

	private static final String NOTIFIE_AREAS = "NOTIFIE_AREAS";
	private Integer qtdNotifiesAreas;

	private static final String NOTIFIE_GROUP = "NOTIFIE_GROUP";
	private Integer qtdNotifiesGroup;

	private static final String NOTIFIE_GRUPOS_PENDENTES = "NOTIFIE_GRUPOS_PENDENTES";
	private Integer qtdNotifiesGruposPendentes;

	private static final String NOTIFIE_MEMBERS = "NOTIFIE_MEMBERS";
	private Integer qtdNotifiesMembers;

	private static final String NOTIFIE_TOTAL = "TOTAL";
	private Integer qtdNotifies;
	
	private static final String NOTIFIE_SUGGEST_AREAS = "NOTIFIE_SUGGEST_AREAS";
	private Integer qtdNotifiesSuggestAreas;

	@PostConstruct
	public void inti() {
		countNotifies();
	}

	public void countNotifies() {
		SimpleDTO simpleDto = notifyService.countNotifications();
		if (simpleDto != null) {
			if (simpleDto.getErrorCode() == 0) {
				try {
					Map<String, String> map = Util.convertToHashMap(simpleDto.getErrorMsg());
					if (!map.isEmpty()) {
						setQtdNotifies(Integer.parseInt(map.get(NOTIFIE_TOTAL)));
						setQtdNotifiesAllMyGroup(Integer.parseInt(map.get(NOTIFIE_ALL_MY_GROUP)));
						setQtdNotifiesAllMyAreas(Integer.parseInt(map.get(NOTIFIE_ALL_MY_AREAS)));
						setQtdNotifiesPessoais(Integer.parseInt(map.get(NOTIFIE_PESSOAIS)));
						if (loginBean.isGi()) {
							setQtdNotifiesAreas(Integer.parseInt(map.get(NOTIFIE_AREAS)));
							setQtdNotifiesGroup(Integer.parseInt(map.get(NOTIFIE_GROUP)));
							setQtdNotifiesGruposPendentes(Integer.parseInt(map.get(NOTIFIE_GRUPOS_PENDENTES)));
							setQtdNotifiesMembers(Integer.parseInt(map.get(NOTIFIE_MEMBERS)));
							setQtdNotifiesSuggestAreas(Integer.parseInt(map.get(NOTIFIE_SUGGEST_AREAS)));
						}
					}

					Util.setPropertySessao("VISUALIZOU", "1");

				} catch (Exception e) {
					setQtdNotifies(0);
				}
			} else
				setQtdNotifies(0);
		} else
			setQtdNotifies(0);
	}

	public Integer getQtdNotifies() {
		return qtdNotifies;
	}

	public void setQtdNotifies(Integer qtdNotifies) {
		this.qtdNotifies = qtdNotifies;
	}

	public Integer getQtdNotifiesAllMyGroup() {
		return qtdNotifiesAllMyGroup;
	}

	public void setQtdNotifiesAllMyGroup(Integer qtdNotifiesAllMyGroup) {
		this.qtdNotifiesAllMyGroup = qtdNotifiesAllMyGroup;
	}

	public Integer getQtdNotifiesPessoais() {
		return qtdNotifiesPessoais;
	}

	public void setQtdNotifiesPessoais(Integer qtdNotifiesPessoais) {
		this.qtdNotifiesPessoais = qtdNotifiesPessoais;
	}

	public Integer getQtdNotifiesAreas() {
		return qtdNotifiesAreas;
	}

	public void setQtdNotifiesAreas(Integer qtdNotifiesAreas) {
		this.qtdNotifiesAreas = qtdNotifiesAreas;
	}

	public Integer getQtdNotifiesGroup() {
		return qtdNotifiesGroup;
	}

	public void setQtdNotifiesGroup(Integer qtdNotifiesGroup) {
		this.qtdNotifiesGroup = qtdNotifiesGroup;
	}

	public Integer getQtdNotifiesGruposPendentes() {
		return qtdNotifiesGruposPendentes;
	}

	public void setQtdNotifiesGruposPendentes(Integer qtdNotifiesGruposPendentes) {
		this.qtdNotifiesGruposPendentes = qtdNotifiesGruposPendentes;
	}

	public Integer getQtdNotifiesMembers() {
		return qtdNotifiesMembers;
	}

	public void setQtdNotifiesMembers(Integer qtdNotifiesMembers) {
		this.qtdNotifiesMembers = qtdNotifiesMembers;
	}

	public Integer getQtdNotifiesSuggestAreas() {
		return qtdNotifiesSuggestAreas;
	}

	public void setQtdNotifiesSuggestAreas(Integer qtdNotifiesSuggestAreas) {
		this.qtdNotifiesSuggestAreas = qtdNotifiesSuggestAreas;
	}

	public Integer getQtdNotifiesAllMyAreas() {
		return qtdNotifiesAllMyAreas;
	}

	public void setQtdNotifiesAllMyAreas(Integer qtdNotifiesAllMyAreas) {
		this.qtdNotifiesAllMyAreas = qtdNotifiesAllMyAreas;
	}

}
