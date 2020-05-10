# QWCFPEnvDependencies
Projeto para manter as dependências de pacotes e componentes usados no QWCFP.

Este documento é como um diário-de-bordo, para documentar tudo o que está sendo feito no ambiente da PIX para promver a integração do QWCFP e o Walli. Por favor a leitura deste documento é obrigatória para garantir a reconstrução do ambiente do QWCFP / Walli.

#### 2020-05-08: QWCFP-Keycloak-10.0.1-2020-05-08.tgz
	Assinatura MD5	/ Arquivo
	f7b8fe3de9325a3410033b971ec709f1  QWCFP-Keycloak-10.0.1-2020-05-08.tgz

Trata-se de KeyCloack instalado e configurado para o ambiente de desenvolvimento, e que portanto está pronto, com todas as dependências aplicado, bastando para isso concatenar na ordem crescente os arquivos e descomprimir para um diretório e por para rodar. **É iportante lembrar** que esta distribuição permite o acesso à administração por qualquer interface da rede, para simplificar os testes durante a fase de Pandemia. Entretanto, ao ser usado em ambiente de produção deverá ser alterado.
##### Como foi criado e restaurar...
	split -a 3 -d  --additional-suffix=_QWCFP-Keycloak-10.0.1-2020-05-08_part \
	      --bytes=80M QWCFP-Keycloak-10.0.1-2020-05-08.tgz
	 
	cat x000_QWCFP-Keycloak-10.0.1-2020-05-08_part \
	    x001_QWCFP-Keycloak-10.0.1-2020-05-08_part \
		x002_QWCFP-Keycloak-10.0.1-2020-05-08_part \
	    > QWCFP-Keycloak-10.0.1-2020-05-08.tgz 
		 
	cd /opt 
	tar -xvzf QWCFP-Keycloak-10.0.1-2020-05-08.tgz	 
		 
#### 2020-05-08: QWCFP-Redis-6.0.1-2020-05-08.tgz
	Assinatura MD5	/ Arquivo
	df0d88e7647127bfd3951e66f1902ce4  QWCFP-Redis-6.0.1-2020-05-08.tgz

Trata-se da ultima versão do REDIS 6.0.0 adicionado dependências no Linux, instalado, compilado e instalado e configurado para o ambiente de desenvolvimento, e que portanto está pronto, com todas as dependências aplicadas, arquivos de configuração do S.O., bastando para isso desempacotar e usar os scripts shell **install-redis.sh** e **limpa-src.sh**. Atenção usar em ambiente de produção, pois esta distribuição permite o acesso à administração por qualquer interface da rede, para simplificar os testes durante a fase de Pandemia. Entretanto, ao ser usado em ambiente de produção deverá ser alterado.

##### Como foi criado e restaurar...
	cd /opt 
	tar -xvzf QWCFP-Redis-6.0.1-2020-05-08.tgz
	# Parametros usados para forçar o aceite de conexão de qualquer interface da rede. **(PERIGO)**
	./bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

##### Artefatos adicionados nos pacotes a seguir
Tratam-se de redundância propositada, ois alterações feitas neles não obriga a fazer um commit no pacote binário inteiro que é grande e 99,9% dele não será alterado.
	09/05/2020  17:59  2.843 **install-redis.sh**
	09/05/2020  17:51  2.919 **Install_REDIS.sh**
	09/05/2020  18:19    178 **limpa-src.sh**
	09/05/2020  17:54 81.536 **redis.conf**
	09/05/2020  17:03    309 **redis.service**

#### 2020-05-08: MASTER-realm-export.json e Template-realm-export.json
São templates JSON para sarem usados em aplicações que desejem submeter RESTFUL ao **Keycloack** para a criação de REALMs (Domínios on Conceito **QWCFP / Walli**), onde cada CNPJ é um Domínio, onde o gestor poderá criar

##### Artefatos adicionados nos pacotes a seguir
	09/05/2020  13:47  44.496 MASTER-realm-export.json
	09/05/2020  13:58  51.551 Template-realm-export.json
