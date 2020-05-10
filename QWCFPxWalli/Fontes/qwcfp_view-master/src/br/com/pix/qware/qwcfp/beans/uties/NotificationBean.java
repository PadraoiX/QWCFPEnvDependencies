package br.com.pix.qware.qwcfp.beans.uties;

import javax.inject.Inject;

import br.com.pix.qware.qwcfp.beans.AbstractBean;
import br.com.pix.qware.qwcfp.beans.LoginBean;
import br.com.pix.qware.qwcfp.service.ConfigService;
import br.com.pix.qware.qwcfp.service.MailService;
import br.com.pix.qware.qwcfp.service.MemberService;
import br.com.pix.qware.qwcfp.service.NotifyService;
import br.com.qwcss.ws.dto.ConfigDTO;
import br.com.qwcss.ws.dto.GroupDTO;
import br.com.qwcss.ws.dto.MemberDTO;

public class NotificationBean extends AbstractBean {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8612210776292184852L;

	@Inject
	private MailService			mailService;
	
	@Inject
	private LoginBean				loginBean;

	@Inject
	private NotifyService		requestService;

	@Inject
	private MemberService		memberService;
	
	@Inject
	private ConfigService		configService;
	
	public void recusaoCadastro(MemberDTO member, String justificativa) {
		if (member != null) {
			if ( member.getMemberEmail() != null) {
				mailService.sendMail(new String[] { member.getMemberEmail() },
						"Notificação QWCFP", justificativa);
			}
		}
	}
	
	
	public void aprovacaoCadastro(MemberDTO member) {
		if (member != null) {
			if ( member.getMemberEmail() != null) {
				mailService.sendMail(new String[] { member.getMemberEmail() },
						"Notificação QWCFP",
						"Prezado senhor(a) " + member.getMemberName()
								+ ", =#0A Parabéns! Foi aprovado o cadastro no sistema.");
			}
		}
	}
	
	public void desaprovacaoCadastro(MemberDTO member) {
		if (member != null) {
			if ( member.getMemberEmail() != null) 
					mailService.sendMail(new String[]{member.getMemberEmail()}, "Notificação QWCFP", "Prezado senhor(a) "+ member.getMemberName() + ", =#0AFoi inativado o seu cadastro no sistema. Para mais informações entrar em contato com o gestor de infra-estrutura: =#0A -Nome: " + loginBean.getLoggedUser().getLoginDto().getMemberName() + "=#0A -Telefone: " + loginBean.getLoggedUser().getLoginDto().getNumberPhone1() + "=#0A -Email: "+ loginBean.getLoggedUser().getLoginDto().getMemberEmail());
		}
	}
	
	
	public void recusaoGrupo(GroupDTO grupo) {
		MemberDTO memberAlterado = null;
		
		if( grupo != null){
			if (grupo.getManagerGroup() != null) {
				memberAlterado = memberService.listar(grupo.getManagerGroup());
			}
			if (memberAlterado != null) {

				if (memberAlterado.getJustificativa() != null && !memberAlterado.getJustificativa().isEmpty() && memberAlterado.getMemberEmail() != null) {
					if(memberAlterado.getJustificativa().equals("S")){
						mailService.sendMail(new String[] { memberAlterado.getMemberEmail() },
								"Notificação QWCFP", grupo.getJustificativa());
					}
				}
			}
		} 
	}

	public void aprovacaoGrupo(GroupDTO grupo) {
		MemberDTO memberAlterado = null;
		
		if( grupo != null){
			if (grupo.getManagerGroup() != null) {
				memberAlterado = memberService.listar(grupo.getManagerGroup());
			}
			if (memberAlterado != null) {

				if (memberAlterado.getMemberEmail() != null) {
					
						mailService.sendMail(new String[] { memberAlterado.getMemberEmail() },
								"Notificação QWCFP",
								"Prezado senhor(a) "
										+ memberAlterado.getMemberName()
										+ ", =#0A Parabéns! O grupo \""
										+ grupo.getNome()
										+ "\" foi aprovado pelo gestor de infra-estrututra, com um espaço de "
										+ grupo.getSizeInBytes()
										+ " bytes. =#0A Atenciosamente,=#0A Equipe "
										+ getTeamName()
										+".");
				}
				requestService.notificateMemberEx(memberAlterado.getMemberId(),
						"Parabéns! O grupo \"" + grupo.getNome()
								+ "\" foi aprovado pelo gestor de infra-estrututra, com um espaço de "
								+ grupo.getSizeInBytes()
								+ " bytes. \n\n Atenciosamente, \n Equipe "
								+ getTeamName()
								+".");
			}
			
		}
	
	}
	
	public void aprovacaoPrivilegios(GroupDTO grupo, MemberDTO memberAlterado, String privilegios){
		if( grupo != null){
			if (memberAlterado != null) {

				if (memberAlterado.getMemberEmail() != null) {
					
						mailService.sendMail(new String[] { memberAlterado.getMemberEmail() },
								"Notificação QWCFP",
								"Prezado senhor(a) "
										+ memberAlterado.getMemberName()
										+ ", =#0A Parabéns! Foi lhe concedido o(s) privilégio(s) de "
										+ privilegios 
										+ ", no grupo \"" + grupo.getNome()
										+ "\"=#0A=#0A Atenciosamente,=#0A Equipe "
										+ getTeamName()
										+".");
					
				}
				
				requestService.notificateMemberEx(memberAlterado.getMemberId(),
						"Prezado senhor(a) "
								+ memberAlterado.getMemberName()
								+ ", \n Parabéns! Foi lhe concedido o(s) privilégio(s) de "
								+ privilegios 
								+ ", no grupo \"" + grupo.getNome() + "\""
								+ " \n\n Atenciosamente,\n Equipe "
								+ getTeamName()
								+".");
			}
			
		}
		
	}
	
	public void aprovacaoPrivilegios(MemberDTO memberAlterado, String message){
		if (memberAlterado != null) {
			if (memberAlterado.getMemberEmail() != null) {
				
				mailService.sendMail(new String[] { memberAlterado.getMemberEmail() },
						"Notificação QWCFP",
						"Prezado senhor(a) "
								+ memberAlterado.getMemberName()
								+ ", =#0A Parabéns! "
								+ message 
								+ "\"=#0A=#0A Atenciosamente,=#0A Equipe "
								+ getTeamName()
								+".");
				
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		
		String a = "ABCDE";
		
		System.out.println(a.substring(1));
		
	}
	
	public String getTeamName() {
		ConfigDTO tn = configService.carregar("TEAM_NAME", "SYSTEM");
		
		if (tn != null && tn.getErrorCode() == 0) {
			return tn.getValue();
		}else {
			return "QWCFP";
		}
	}

}
