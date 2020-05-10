	$(document).ready( function() {
		$("#SuperServerLogin").validate({
			rules:{
				user:{
					required: true, minlength: 11, verificaCPF: true
				},
				pass:{
					required: true, minlength: 5
				}
			},
			messages:{
				user:{
					required: "Digite o seu CPF",
					minLength: "O seu CPF deve conter, no mínimo, 11 caracteres"
				},
				pass:{
					required: "Digite a sua senha",
					minLength: "A sua mensagem deve conter, no mínimo, 5 caracteres"
				}
			}
		});
		
		$("SuperServerLogin").submit(function() {
			pwEncrypt = $().crypt( {
					method: 'md5',
					source: $('#pass').val()
				});
			alert(pwEncrypt);
			$("SuperServerLogin").submit();
			return true;
		});
	});
