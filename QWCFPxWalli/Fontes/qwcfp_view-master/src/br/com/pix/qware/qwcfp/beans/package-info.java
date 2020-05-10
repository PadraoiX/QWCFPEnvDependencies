/**
 * <h1>PROJETO IMPLEMENTANDO A ESPECIFICAÇÃO JPA</h1>
 */
/**
 * @author youre.fernandez
 *
 */
package br.com.pix.qware.qwcfp.beans;

/**
 * <h3>Neste pacote se encontra os beans, que serão referenciados pelas páginas xhtml.
 * Lembrando que os Beans não contém lógica de negocio, somente chama os métodos das
 * classes Services.</h3>
 */

/**
*<h1>Bean:</h1> 
*
*<p>Classe java que implementa a interface java.io.Serializable;
*Todo atributo dele deve ter seus respetivos Getters and Setters;
*Deve ser declarado como "Bean" atraves da annotation "@Named",
*Deve ter defino o escopo dele com as seguintes annotations
*do pacote javax.enterprise.context.SessionScoped:
*
* => "@RequestScoped": O Bean definido com este escopo será criado quando uma 
*     requisição HTTP ocorre e é destuído assim que a resposta da requisição é enviada ao cliente;
* => "@SessionScoped": O Bean existe enquanto durar a sessão do usuário,
*     múltipplas requisições do mesmo cliente. A sessão termina quando o navegador é fechado ou via progrmador;
* => "@ApplicationScoped": O Bean  compartilhado entre todos os clientes que acessam a aplicação,
*     permanece ativo enquanto a aplicação tiver executando;
* => "@EscopoConversation" O Bean existe durante um período de conversação, definido pelo programador.
*     Este escopo é diferente da sessão, a sessão do usuário pode ter mais de uma conversação;
*</p>
*
*<h3>Para saber mais sobre Beans e escopos procure o curso de JSF oferecido pela softblue
*Para ter acesso deve-se realizar o login com o user: you260795@gmail.com e senha:pix2000
*</h3>
*/

