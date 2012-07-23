package my.projects.contactbook.client;

import java.lang.ref.PhantomReference;
import java.util.*;

import my.projects.contactbook.shared.FieldVerifier;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.Phone;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellWidget;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class ContactBook implements EntryPoint {
	private static int id=0;
	private static Long selectedIndex;
	private List<Contact> CONTACTS=new ArrayList<Contact>();
	private CellTable<Contact> table = new CellTable<Contact>();
	private ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	boolean added;
	static class ColorCell extends AbstractCell<List<Phone>> {

	    /**
	     * The HTML templates used to render the cell.
	     */
	    interface Templates extends SafeHtmlTemplates {
	      /**
	       * The template for this Cell, which includes styles and a value.
	       * 
	       * @param styles the styles to include in the style attribute of the div
	       * @param value the safe value. Since the value type is {@link SafeHtml},
	       *          it will not be escaped before including it in the template.
	       *          Alternatively, you could make the value type String, in which
	       *          case the value would be escaped.
	       * @return a {@link SafeHtml} instance
	       */
	      @SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
	      SafeHtml cell(SafeStyles styles, SafeHtml value);
	    }

	    /**
	     * Create a singleton instance of the templates used to render the cell.
	     */
	    private static Templates templates = GWT.create(Templates.class);

	    @Override
	    public void render(Context context, List<Phone> phones, SafeHtmlBuilder sb) {
	      /*
	       * Always do a null check on the value. Cell widgets can pass null to
	       * cells if the underlying data contains a null, or if the data arrives
	       * out of order.
	       */
	      if (phones == null) {
	        return;
	      }

	      for(Phone phone:phones){
	      // If the value comes from the user, we escape it to avoid XSS attacks.
	      SafeHtml safeValue = SafeHtmlUtils.fromString(phone.getNumber()+"("+phone.getType()+")");

	      // Use the template to create the Cell's html.
	      SafeStyles styles = SafeStylesUtils.fromTrustedString("width: 100%;");
	      SafeHtml rendered = templates.cell(styles, safeValue);
	      sb.append(rendered);
	      }
	    }
	  }
	
	private GreetingServiceAsync service=GWT.create(GreetingService.class);
	
	  public void onModuleLoad() {
		 
		   table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
			service.list(new AsyncCallback<List<Contact>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(List<Contact> result) {
					
					CONTACTS=result;

				    List<Contact> list = dataProvider.getList();

			        for(Contact c:result)
			        list.add(c);
				    					
				}
			});
					
	    System.out.println(CONTACTS.size());
	   TextColumn<Contact> idColumn = new TextColumn<Contact>() {
		      @Override
		      public String getValue(Contact object) {
		        return object.getId()+"";
		      }
		    };
		    table.addColumn(idColumn, "Id");
	    
	    TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
		      @Override
		      public String getValue(Contact object) {
		        return object.getSurname();
		      }
		    };
		    table.addColumn(surnameColumn, "Surname");
	    // Add a text column to show the name.
	    TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
	      @Override
	      public String getValue(Contact object) {
	        return object.getName();
	      }
	    };
	    table.addColumn(nameColumn, "Name");

	    // Add a date column to show the surname.
	    ColorCell cl=new ColorCell();
	    Column<Contact, List<Phone>> dateColumn = new Column<Contact, List<Phone>>(cl) {

			@Override
			public List<Phone> getValue(Contact object) {
				// TODO Auto-generated method stub
				return object.getPhones();
			}
		};
	    table.addColumn(dateColumn, "Phones");

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
	    table.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        Contact selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	         selectedIndex=selected.getId();
	        }
	      }
	    });
	    
	    dataProvider.addDataDisplay(table);
	    
	    Button add = new Button("+", new ClickListener() {
	        public void onClick(Widget sender) {
	           DialogBox dlg = new MyDialog("add");
	           dlg.center();
	        }
	      });
	    
	   add.setStyleName("addButton");
	    Button edit = new Button("..", new ClickListener() {
	        public void onClick(Widget sender) {
	           System.out.println(selectedIndex);
	           if(selectedIndex>0){
	           DialogBox dlg = new MyDialog("edit");
	           dlg.center();
	           }
	           else
	           {
	        	   DialogBox dlg=new ExceptionDialog();
	        	   dlg.center();
	           }
	        }
	      });
	    edit.setStyleName("editButton");
	    Button remove = new Button("-", new ClickListener() {
	        public void onClick(Widget sender) {
	        	 if(selectedIndex>0){
	        	final List<Contact> list = dataProvider.getList();

	        	
	        	for(final Contact c:list)
			    	if(c.getId()==selectedIndex){
			    		service.delete(c,new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert(caught.getMessage());
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								list.remove(c);
					    		table.redraw();
								Window.alert("Selected contact is removed!");
							}
						});
			    		
			    	}
			    	
	        }
	        	 else
		           {
		        	   DialogBox dlg=new ExceptionDialog();
		        	   dlg.center();
		           }
	        }
	      });
	    remove.setStyleName("removeButton");
	    VerticalPanel actionPanel=new VerticalPanel();
	    actionPanel.add(add);
	    DOM.setStyleAttribute(add.getElement(), "margin","5px" );
	    //DOM.setStyleAttribute(add.getElement(), "margin-top","25px" );
	    DOM.setStyleAttribute(add.getElement(), "width","25px" );
	    actionPanel.add(edit);
	    DOM.setStyleAttribute(edit.getElement(), "margin","5px" );
	    DOM.setStyleAttribute(edit.getElement(), "width","25px" );
	    actionPanel.add(remove);
	    DOM.setStyleAttribute(remove.getElement(), "margin","5px" );
	    DOM.setStyleAttribute(remove.getElement(), "width","25px" );
	    
	    HorizontalPanel mainPanel=new HorizontalPanel();
	    mainPanel.add(table);
	    mainPanel.add(actionPanel);
	    table.setWidth("90%");
	    DOM.setStyleAttribute(table.getElement(), "margin","20px" );
	    mainPanel.setWidth("80%");
	    RootPanel.get("root").add(mainPanel);
	      
	  }
	  class MyDialog extends DialogBox implements ClickListener {
		  
		  public MyDialog(String action) {
			  if(action.equals("add")){
		    setText("Sample DialogBox");
		    
		    InlineLabel surnameLabel=new InlineLabel("Surname");
		     final TextBox surname=new TextBox();
		    InlineLabel nameLabel=new InlineLabel("Name");
		    final TextBox name=new TextBox();
		    
		    
		    final Grid phonesPanel=new Grid(0, 4);
		    
		    InlineLabel phoneLabel=new InlineLabel("Phones");
		  
		    Button addPhoneButton = new Button("Add phone", new ClickListener() {
				
				@Override
				@Deprecated
				public
				void onClick(Widget sender) {
					// TODO Auto-generated method stub
					
					TextBox phone=new TextBox();
					
					InlineLabel l=new InlineLabel("Phone");
					ListBox lb=new ListBox();
					lb.addItem("mobile");
				    lb.addItem("home");
				    lb.addItem("work");
				    Button deletePhone=new Button("X",new ClickListener() {
						
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
				   
				    deletePhone.setHeight("25%");
				    deletePhone.setWidth("15%");
				    int index=phonesPanel.getRowCount();
				    l.setLayoutData(index);
				    phone.setLayoutData(index);
				    lb.setLayoutData(index);
				    deletePhone.setLayoutData(index);
				    phonesPanel.insertRow(index);
				   
				    phonesPanel.setWidget(index, 0, l);
				    phonesPanel.setWidget(index, 1, phone);
				    phonesPanel.setWidget(index, 2, lb);
				    phonesPanel.setWidget(index, 3, deletePhone);
				    
				}
		    });
		    
		    Button okButton = new Button("OK", new ClickListener() {
				
				@Override
				@Deprecated
				public
				void onClick(Widget sender) {
					// TODO Auto-generated method stub
					
					final List<Contact> list = dataProvider.getList();
					final Contact c=new Contact();
					c.setName(name.getValue());
					c.setSurname(surname.getValue());
					
					List<Phone> phones=new ArrayList<Phone>();
					for(int i=0;i<phonesPanel.getRowCount();i++){
						TextBox number=(TextBox) phonesPanel.getWidget(i,1);
						ListBox type=(ListBox) phonesPanel.getWidget(i,2);
													
						Phone newPhone=new Phone();
						newPhone.setNumber(number.getValue());
						newPhone.setType(type.getItemText(type.getSelectedIndex()));
						
						phones.add(newPhone);
						
					}
					c.setPhones(phones);
					added=false;
			        service.insert(c, new AsyncCallback<Long>() {
						
						@Override
						public void onSuccess(Long result) {
							c.setId(result);
							list.add(c);
							table.redraw();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}
					});
				   
			        	
			        
				    hide();
				    	
				}
			});
		    Button closeButton = new Button("Close", this);
		  
		   FlexTable flexTable = new FlexTable();
		    flexTable.setWidget(0, 0, surnameLabel);
		    flexTable.setWidget(0, 1, surname);
		    flexTable.setWidget(1, 0, nameLabel);
		    flexTable.setWidget(1, 1, name);
		    flexTable.setWidget(2, 0, phoneLabel);
		    flexTable.setWidget(2, 1, addPhoneButton);
		    flexTable.setWidget(3, 0, phonesPanel);
		    flexTable.setWidget(4, 1, okButton);
		    flexTable.setWidget(4, 2, closeButton);
		    flexTable.setStyleName("panel flexTable");
		    flexTable.getFlexCellFormatter().setColSpan(3, 0, 3);
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
				    setText("Sample DialogBox");
				    Label surnameLabel=new Label("Surname");
				    
				    final TextBox surname=new TextBox();
				    
				    Label nameLabel=new Label("Name");
				    final TextBox name=new TextBox();
				    List<Contact> list = dataProvider.getList();

				    for(Contact c:list)
				    	if(c.getId()==selectedIndex){
				    surname.setValue(c.getSurname());
				    name.setValue(c.getName());
				    
				    	}
				    
				    final Grid phonesPanel=new Grid(0, 4);
				    
				    for(Contact c:list)
				    	if(c.getId()==selectedIndex){
				    		for(int i=0;i<c.getPhones().size();i++){
				    		TextBox phone=new TextBox();
				    		phone.setValue(c.getPhones().get(i).getNumber());
							InlineLabel l=new InlineLabel("Phone");
							ListBox lb=new ListBox();
							lb.addItem("mobile");
						    lb.addItem("home");
						    lb.addItem("work");
						    Button deletePhone=new Button("X",new ClickListener() {
								
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
						    deletePhone.setHeight("25%");
						    deletePhone.setWidth("15%");
						    for(int j=0;j<lb.getItemCount();j++)
						    if(c.getPhones().get(i).getType().equals(lb.getItemText(j)))
						    		lb.setSelectedIndex(j);
						    int index=phonesPanel.getRowCount();
						    l.setLayoutData(index);
						    phone.setLayoutData(index);
						    lb.setLayoutData(index);
						    deletePhone.setLayoutData(index);
						    phonesPanel.insertRow(index);
						    phonesPanel.setWidget(index, 0, l);
						    phonesPanel.setWidget(index, 1, phone);
						    phonesPanel.setWidget(index, 2, lb);
						    phonesPanel.setWidget(index, 3, deletePhone);
						    
				    		}
				    	}
				    
				    InlineLabel phoneLabel=new InlineLabel("Phones");
				    
				    
				    final Contact c=new Contact();
					
				    Button addPhoneButton = new Button("Add phone", new ClickListener() {
						
						@Override
						@Deprecated
						public
						void onClick(Widget sender) {
							// TODO Auto-generated method stub
							TextBox phone=new TextBox();
							InlineLabel l=new InlineLabel("Phone");
							ListBox lb=new ListBox();
							lb.addItem("mobile");
						    lb.addItem("home");
						    lb.addItem("work");
						    Button deletePhone=new Button("X",new ClickListener() {
								
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
						    deletePhone.setHeight("25%");
						    deletePhone.setWidth("15%");
						    
						    int index=phonesPanel.getRowCount();
						    l.setLayoutData(index);
						    phone.setLayoutData(index);
						    lb.setLayoutData(index);
						    deletePhone.setLayoutData(index);
						    phonesPanel.insertRow(index);
						    phonesPanel.setWidget(index, 0, l);
						    phonesPanel.setWidget(index, 1, phone);
						    phonesPanel.setWidget(index, 2, lb);
						    phonesPanel.setWidget(index, 3, deletePhone);
						    
						}
					});
				    addPhoneButton.setHeight("30%");
				    
				    Button okButton = new Button("OK", new ClickListener() {
						
						@Override
						@Deprecated
						public
						void onClick(Widget sender) {
							// TODO Auto-generated method stub
							List<Contact> list = dataProvider.getList();

							for(Contact c:list)
						    	if(c.getId()==selectedIndex){
						    c.setSurname(surname.getValue());
						    c.setName(name.getValue());
						    c.getPhones().clear();
						    for(int i=0;i<phonesPanel.getRowCount();i++){
								TextBox number=(TextBox) phonesPanel.getWidget(i,1);
								ListBox type=(ListBox) phonesPanel.getWidget(i,2);
								Phone newPhone=new Phone();
								newPhone.setNumber(number.getValue());
								newPhone.setType(type.getItemText(type.getSelectedIndex()));
								c.addPhone(newPhone);
								service.update(c, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert(caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										table.redraw();
									}
								});
								}
						    
						    hide();
						    	}
						}
					});
				    
				    Button closeButton = new Button("Close", this);
				    FlexTable flexTable = new FlexTable();
				    flexTable.setWidget(0, 0, surnameLabel);
				    flexTable.setWidget(0, 1, surname);
				    flexTable.setWidget(1, 0, nameLabel);
				    flexTable.setWidget(1, 1, name);
				    flexTable.setWidget(2, 0, phoneLabel);
				    flexTable.setWidget(2, 1, addPhoneButton);
				    flexTable.setWidget(3, 0, phonesPanel);
				    flexTable.setWidget(4, 1, okButton);
				    flexTable.setWidget(4, 2, closeButton);
				    flexTable.setStyleName("panel flexTable");
				    flexTable.getFlexCellFormatter().setColSpan(3, 0, 3);
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
		  public void onClick(Widget sender) {
		    hide();
		  }
		 

		}
	  class ExceptionDialog extends DialogBox implements ClickListener {
		  public ExceptionDialog() {
			
		    setText("No contact is selected");
		    
		    InlineLabel message=new InlineLabel("Please choose the contact to make changes!");
		     VerticalPanel vp=new VerticalPanel();
		  Button ok=new Button("OK",new ClickListener() {
			
			@Override
			@Deprecated
			public
			void onClick(Widget sender) {
				// TODO Auto-generated method stub
				hide();
			}
		});
		  DOM.setElementAttribute(ok.getElement(), "id", "ok");
		    vp.add(message);
		    vp.add(ok);
		    setWidget(vp);
			  
		  }
		@Override
		@Deprecated
		public
		void onClick(Widget sender) {
			// TODO Auto-generated method stub
			hide();
		}
		  
		  
	  }
	  public AbsolutePanel setHidden(AbsolutePanel ap,boolean hidden)
	  {	      
	      String val = (hidden) ? "hidden" : "visible";
	      DOM.setStyleAttribute(ap.getElement(), "overflow", val);
		return ap;
	  }
	}