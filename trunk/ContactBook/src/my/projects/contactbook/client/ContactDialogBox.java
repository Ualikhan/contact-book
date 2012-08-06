package my.projects.contactbook.client;

import java.util.ArrayList;
import java.util.List;

import my.projects.contactbook.shared.FieldVerifier;
import my.projects.contactbook.shared.Phone;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ContactDialogBox extends DialogBox implements ClickListener {
	 
	
	DialogBox current;
		  Grid phonesPanel;
		  
		  public ContactDialogBox(String action) {
			  setPopupPosition(getPopupLeft()-100, getPopupTop());
			  current=this;
			  
			  if(action.equals("add")){
				  
		    setText("Add contact");
	
		   
		    
		    InlineLabel surnameLabel=new InlineLabel("Surname");
		     final TextBox surname=new TextBox();
		    InlineLabel nameLabel=new InlineLabel("Name");
		    final TextBox name=new TextBox();
		    
		    phonesPanel=new Grid(0, 5);
		    
		    InlineLabel phoneLabel=new InlineLabel("Phones");
		 
		    Button addPhoneButton = new Button();
		    addPhoneButton.addClickListener(new ClickListener() {
				
				@Override
				@Deprecated
				public
				void onClick(Widget sender) {
					// TODO Auto-generated method stub

					current.setPopupPosition(getPopupLeft(), getPopupTop()-10);
					TextBox phoneNumber=new TextBox();
					
					InlineLabel phoneLabel=new InlineLabel("Phone");
					ListBox phoneType=new ListBox();
					phoneType.addItem("mobile");
				    phoneType.addItem("home");
				    phoneType.addItem("work");
				    
				    Button chooseNumberButton=new Button();
				    chooseNumberButton.addClickListener(new ClickListener() {
						
						@Override
						@Deprecated
						public void onClick(Widget sender) {
							// TODO Auto-generated method stub
							sender.setStyleName("chooseNumberButtonActive");
							ChoosePhoneNumberDialogBox pd=new ChoosePhoneNumberDialogBox();
							pd.show();
							pd.setPopupPosition(getPopupLeft()+getOffsetWidth()-20,sender.getAbsoluteTop()-60);
							pd.setArgument1(sender.getLayoutData());
							pd.setArgument2(current);
							
						}
					});
				     
				    Button deletePhoneButton=new Button();
				    deletePhoneButton.addClickListener(new ClickListener() {
						
						@Override
						@Deprecated
						public
						void onClick(Widget sender) {
							// TODO Auto-generated method stub
							//int removeIndex=Integer.parseInt(sender.getTitle());
							for(int i=0;i<phonesPanel.getRowCount();i++)
								if(phonesPanel.getWidget(i, 0).getLayoutData().toString().equals(sender.getLayoutData().toString())){
								phonesPanel.removeRow(i);
							}
								
								
						}
					});
				    chooseNumberButton.setStyleName("chooseNumberButton");
				    deletePhoneButton.setStyleName("deletePhoneButton");
				   
			
				    int index=phonesPanel.getRowCount();
				    phoneLabel.setLayoutData(index);
				    phoneNumber.setLayoutData(index);
				    
				    phoneType.setLayoutData(index);
				    chooseNumberButton.setLayoutData(index);
				    
				    deletePhoneButton.setLayoutData(index);
				    phonesPanel.insertRow(index);
				   
				    phonesPanel.setWidget(index, 0, phoneLabel);
				    phonesPanel.setWidget(index, 1, phoneNumber);
				    phonesPanel.setWidget(index, 2, phoneType);
				    phonesPanel.setWidget(index, 3, deletePhoneButton);
				    phonesPanel.setWidget(index, 4, chooseNumberButton);
					   
				}
		    });
		    addPhoneButton.setStyleName("addPhoneButton");
		    Button okButton = new Button("OK", new ClickListener() {
				
				@Override
				@Deprecated
				public
				void onClick(Widget sender) {
					// TODO Auto-generated method stub
					boolean validNumbers=true;
					boolean validName=true;
					boolean validSurname=true;
					final List<Contact> list = ContactBook.dataProvider.getList();
					final Contact c=new Contact();
					if(FieldVerifier.isValidName(name.getValue()))
					c.setName(name.getValue());
					else validName=false;
					if(FieldVerifier.isValidSurname(name.getValue()))
					c.setSurname(surname.getValue());
					else validSurname=false;
					List<Phone> phones=new ArrayList<Phone>();
					for(int i=0;i<phonesPanel.getRowCount();i++){
						TextBox number=(TextBox) phonesPanel.getWidget(i,1);
						ListBox type=(ListBox) phonesPanel.getWidget(i,2);
													
						Phone newPhone=new Phone();
						
						newPhone.setNumber(number.getValue());
						newPhone.setType(type.getItemText(type.getSelectedIndex()));
						if(FieldVerifier.isValidNumber(newPhone.getNumber()))
						phones.add(newPhone);
						
					}
					if(validNumbers){
					c.setPhones(phones);
					if(validName && validSurname){
			        ContactBook.service.insert(c, new AsyncCallback<Long>() {
						
						@Override
						public void onSuccess(Long result) {
							c.setId(result);
							list.add(c);
							ContactBook.addNewContact();
							ContactBook.table.redraw();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}
					});
					}
					else if(!validName)
						name.setValue("Enter valid name");
					else if(!validSurname)
						surname.setValue("Enter valid surname");
					
				    hide();
					}
						
					
				}
			});
		    Button closeButton = new Button("Close", this);
		    
		    
		    HorizontalPanel hp=new HorizontalPanel();
		    hp.setStyleName("hp");
		    okButton.setStyleName("okButtonDialog");
		    closeButton.setStyleName("closeButtonDialog");
		    hp.add(okButton);
		    hp.add(closeButton);
		    
		   FlexTable flexTable = new FlexTable();
		    flexTable.setWidget(0, 0, surnameLabel);
		    flexTable.setWidget(0, 1, surname);
		    flexTable.setWidget(1, 0, nameLabel);
		    flexTable.setWidget(1, 1, name);
		    flexTable.setWidget(2, 0, phoneLabel);
		    flexTable.setWidget(2, 1, addPhoneButton);
		    flexTable.setWidget(3, 0, phonesPanel);
		    flexTable.setWidget(4, 0, hp);
		    
		    
		    flexTable.setStyleName("panel flexTable");
		    flexTable.getFlexCellFormatter().setColSpan(3, 0, 3);
		    flexTable.getFlexCellFormatter().setColSpan(4, 0, 3);
		    
		    for (int i = 0; i < flexTable.getRowCount(); i++) {
		        for (int j = 0; j < flexTable.getCellCount(i); j++) {
		            if ((j % 2) == 0) {
		                flexTable.getFlexCellFormatter()
		                         .setStyleName(i, j, "tableCell-even");
		            } else {
		                flexTable.getFlexCellFormatter()
		                         .setStyleName(i, j, "tableCell-odd");
		            }
		        }
		    }

		    setWidget(flexTable);
		  }

			  else if(action.equals("edit")){
				    setText("Edit contact");
				    Label surnameLabel=new Label("Surname");
				    
				    final TextBox surname=new TextBox();
				    
				    Label nameLabel=new Label("Name");
				    final TextBox name=new TextBox();
				    List<Contact> list = ContactBook.dataProvider.getList();

				    for(Contact c:list)
				    	if(c.getId()==ContactBook.selectedIndex){
				    surname.setValue(c.getSurname());
				    name.setValue(c.getName());
				    
				    	}
				    
				    phonesPanel=new Grid(0, 5);
				    
				    for(Contact c:list)
				    	if(c.getId()==ContactBook.selectedIndex){
				    		for(int i=0;i<c.getPhones().size();i++){
				    		TextBox phoneNumber=new TextBox();
				    		phoneNumber.setValue(c.getPhones().get(i).getNumber());
							InlineLabel phoneLabel=new InlineLabel("Phone");
							ListBox phoneType=new ListBox();
							phoneType.addItem("mobile");
						    phoneType.addItem("home");
						    phoneType.addItem("work");
						    Button chooseNumberButton=new Button();
						    chooseNumberButton.addClickListener(new ClickListener() {						
								@Override
								@Deprecated
								public void onClick(Widget sender) {
									// TODO Auto-generated method stub
									ChoosePhoneNumberDialogBox pd=new ChoosePhoneNumberDialogBox();
									current.setPopupPosition(getPopupLeft()-120, getPopupTop());
									pd.show();
									pd.setPopupPosition(getPopupLeft()+getOffsetWidth()-20,sender.getAbsoluteTop()-60);
									pd.setArgument1(sender.getLayoutData());
									pd.setArgument2(current);
								}
							});
						    
						    Button deletePhoneButton=new Button();
						    deletePhoneButton.addClickListener(new ClickListener() {
								
								@Override
								@Deprecated
								public
								void onClick(Widget sender) {
									// TODO Auto-generated method stub
									//int removeIndex=Integer.parseInt(sender.getTitle());
									for(int i=0;i<phonesPanel.getRowCount();i++)
										if(phonesPanel.getWidget(i, 0).getLayoutData().toString().equals(sender.getLayoutData().toString())){
										phonesPanel.removeRow(i);
									}									
										
								}
							});
						    chooseNumberButton.setStyleName("chooseNumberButton");
						    deletePhoneButton.setStyleName("deletePhoneButton");
						    
						    for(int j=0;j<phoneType.getItemCount();j++)
						    if(c.getPhones().get(i).getType().equals(phoneType.getItemText(j)))
						    		phoneType.setSelectedIndex(j);
						    int index=phonesPanel.getRowCount();
						    phoneLabel.setLayoutData(index);
						    phoneNumber.setLayoutData(index);
						   
						    phoneType.setLayoutData(index);
						    deletePhoneButton.setLayoutData(index);
						    chooseNumberButton.setLayoutData(index);
						    phonesPanel.insertRow(index);
						    phonesPanel.setWidget(index, 0, phoneLabel);
						    phonesPanel.setWidget(index, 1, phoneNumber);
						    phonesPanel.setWidget(index, 2, phoneType);
						    phonesPanel.setWidget(index, 3, deletePhoneButton);
						    phonesPanel.setWidget(index, 4, chooseNumberButton);
				    		}
				    	}
				    
				    InlineLabel phoneLabel=new InlineLabel("Phones");
				    				    
				    final Contact c=new Contact();
					
				    Button addPhoneButton = new Button();
				    addPhoneButton.addClickListener(new ClickListener() {
						
						@Override
						@Deprecated
						public
						void onClick(Widget sender) {
							// TODO Auto-generated method stub
							current.setPopupPosition(getPopupLeft(), getPopupTop()-10);
							
							TextBox phoneNumber=new TextBox();
							InlineLabel phoneLabel=new InlineLabel("Phone");
							ListBox phoneType=new ListBox();
							phoneType.addItem("mobile");
						    phoneType.addItem("home");
						    phoneType.addItem("work");
						    
						    Button chooseNumberButton=new Button();
						    chooseNumberButton.addClickListener(new ClickListener() {
								
								@Override
								@Deprecated
								public void onClick(Widget sender) {
									// TODO Auto-generated method stub
									ChoosePhoneNumberDialogBox pd=new ChoosePhoneNumberDialogBox();
									current.setPopupPosition(getPopupLeft()-120, getPopupTop());
									pd.show();
									pd.setPopupPosition(getPopupLeft()+getOffsetWidth()-20,sender.getAbsoluteTop()-60);
									pd.setArgument1(sender.getLayoutData());
									pd.setArgument2(current);
									
								}
							});
						    Button deletePhoneButton=new Button();
						    deletePhoneButton.addClickListener(new ClickListener() {
								
								@Override
								@Deprecated
								public
								void onClick(Widget sender) {
									// TODO Auto-generated method stub
									//int removeIndex=Integer.parseInt(sender.getTitle());
									for(int i=0;i<phonesPanel.getRowCount();i++)
										if(phonesPanel.getWidget(i, 0).getLayoutData().toString().equals(sender.getLayoutData().toString())){
										phonesPanel.removeRow(i);
									}
										
										
								}
							});
						    chooseNumberButton.setStyleName("chooseNumberButton");
						    deletePhoneButton.setStyleName("deletePhoneButton");
						    
						    int index=phonesPanel.getRowCount();
						    phoneLabel.setLayoutData(index);
						    phoneNumber.setLayoutData(index);
						    
						    phoneType.setLayoutData(index);
						    chooseNumberButton.setLayoutData(index);
						    deletePhoneButton.setLayoutData(index);
						    phonesPanel.insertRow(index);
						    phonesPanel.setWidget(index, 0, phoneLabel);
						    phonesPanel.setWidget(index, 1, phoneNumber);
						    phonesPanel.setWidget(index, 2, phoneType);
						    phonesPanel.setWidget(index, 3, deletePhoneButton);
						    phonesPanel.setWidget(index, 4, chooseNumberButton);
						}
					});
				      
				    addPhoneButton.setStyleName("addPhoneButton");
				    
				    Button okButton = new Button("OK", new ClickListener() {
						
						@Override
						@Deprecated
						public
						void onClick(Widget sender) {
							// TODO Auto-generated method stub
							
							List<Contact> list = ContactBook.dataProvider.getList();
							boolean validNumbers=true;
							boolean validName=true;
							boolean validSurname=true;
							
							for(Contact c:list)
						    	if(c.getId()==ContactBook.selectedIndex){
						    if(FieldVerifier.isValidName(name.getValue()) && !name.getValue().equals("Enter valid name!"))		
						    	c.setName(name.getValue());
						    else validName=false;
							
						    if(FieldVerifier.isValidSurname(surname.getValue())&& !name.getValue().equals("Enter valid surname!"))		
						    	 c.setSurname(surname.getValue());
						      	else validSurname=false;
							
						    c.getPhones().clear();
						    for(int i=0;i<phonesPanel.getRowCount();i++){
								TextBox number=(TextBox) phonesPanel.getWidget(i,1);
								ListBox type=(ListBox) phonesPanel.getWidget(i,2);
								Phone newPhone=new Phone();
								if(FieldVerifier.isValidNumber(number.getValue())){
								newPhone.setNumber(number.getValue());
								newPhone.setType(type.getItemText(type.getSelectedIndex()));
								c.addPhone(newPhone);
							
								}
								
								
								}
						    if(validName && validSurname){
						        
						    	ContactBook.service.update(c, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert(caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										ContactBook.table.redraw();
									}
								});	
						    
						    hide();
						    }
						    else if(!validName)
								name.setValue("Enter valid name!");
							else if(!validSurname)
								surname.setValue("Enter valid surname!");
						    	}
						}
					});
				    
				    Button closeButton = new Button("Close", this);
				    
				    HorizontalPanel hp=new HorizontalPanel();
				    hp.add(okButton);
				    hp.add(closeButton);
				    
				    FlexTable flexTable = new FlexTable();
				    flexTable.setWidget(0, 0, surnameLabel);
				    flexTable.setWidget(0, 1, surname);
				    flexTable.setWidget(1, 0, nameLabel);
				    flexTable.setWidget(1, 1, name);
				    flexTable.setWidget(2, 0, phoneLabel);
				    flexTable.setWidget(2, 1, addPhoneButton);
				    flexTable.setWidget(3, 0, phonesPanel);
				    flexTable.setWidget(4, 0,hp);
				    
				    hp.setStyleName("hp");
				    okButton.setStyleName("okButtonDialog");
				    closeButton.setStyleName("closeButtonDialog");
				   
				    
				    flexTable.setStyleName("panel flexTable");
				    flexTable.getFlexCellFormatter().setColSpan(3, 0, 3);
				    flexTable.getFlexCellFormatter().setColSpan(4, 0, 3);
				    
				    for (int i = 0; i < flexTable.getRowCount(); i++) {
				        for (int j = 0; j < flexTable.getCellCount(i); j++) {
				            if ((j % 2) == 0) {
				                flexTable.getFlexCellFormatter()
				                         .setStyleName(i, j, "tableCell-even");
				            } else {
				                flexTable.getFlexCellFormatter()
				                         .setStyleName(i, j, "tableCell-odd");
				            }
				        }
				    }
				    
				    
				    setWidget(flexTable);
				  }
			  
		  }
		  public void setNumberText(Object ldata,String num){
			  for(int i=0;i<phonesPanel.getRowCount();i++){
				  if(phonesPanel.getWidget(i, 1).getLayoutData().equals(ldata)){
					TextBox number = (TextBox) phonesPanel.getWidget(i,1);
			  		number.setValue(num);
				  }
		  }
		  }
		  public void setStyle(){
			  for(int i=0;i<phonesPanel.getRowCount();i++){
				  
					Button sender =  (Button) phonesPanel.getWidget(i,4);
			  		sender.setStyleName("chooseNumberButton");
				  
		  }
		  }
		  public void onClick(Widget sender) {	
		    hide();
		  }
		 
		}

