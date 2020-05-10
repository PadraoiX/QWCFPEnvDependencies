package br.com.pix.qware.qwcfp.util;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
 
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
 
@ManagedBean
@SessionScoped
public class Teste {
    // TreeNode instance
    private TreeNode root;

    public Teste(){
        this.root = new DefaultTreeNode("Root Node", null);
        
        TreeNode node0 = new DefaultTreeNode("Meus Grupos", this.root);
        
        TreeNode node00 = new DefaultTreeNode("Grupo 01", node0);
        /*TreeNode node01 = new DefaultTreeNode("Grupo 02", node0);
        TreeNode node02 = new DefaultTreeNode("Grupo 03", node0);
        TreeNode node03 = new DefaultTreeNode("Grupo 04", node0);
        
        node01.getChildren().add(new DefaultTreeNode("SubGrupo01"));
        node01.getChildren().add(new DefaultTreeNode("SubGrupo02"));
        node01.getChildren().add(new DefaultTreeNode("SubGrupo03"));
        
        node03.getChildren().add(new DefaultTreeNode("SubGrupo01"));
        node03.getChildren().add(new DefaultTreeNode("SubGrupo02"));*/
        
    }
 
    public TreeNode getRoot() {
        return root;
    }
 
    public void setRoot(TreeNode root) {
        this.root = root;
    }

}