/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.moveINSA;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Coucou")
public class VuePrincipale extends VerticalLayout{
    
    private Button vbCoucou;
    
    public VuePrincipale() {
        this.vbCoucou = new Button("dis Coucou");
        this.vbCoucou.addClickListener((t) -> {
            Notification.show("Coucou");
        });
        this.add(this.vbCoucou);
    }   
}