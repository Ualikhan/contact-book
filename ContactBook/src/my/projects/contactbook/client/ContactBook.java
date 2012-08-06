package my.projects.contactbook.client;

import java.lang.ref.PhantomReference;
import java.util.*;

import my.projects.contactbook.shared.FieldVerifier;
import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;
import my.projects.contactbook.shared.Phone;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ButtonCell;
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
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
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
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class ContactBook implements EntryPoint {
	
	InlineLabel searchLabel;
	TextBox searchText;
	static Long selectedIndex;
	private static int pageNumber;
	private static int selectedLink=0;
	static HorizontalPanel thirdPanel;
	private boolean success;
	static CellTable<Contact> table = new CellTable<Contact>();
    static ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
	static class PhoneCell extends AbstractCell<List<Phone>> {

	  interface Templates extends SafeHtmlTemplates {
	     
	      @SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
	      SafeHtml cell(SafeStyles styles, SafeHtml value);
	    }

	    /**
	     * Create a singleton instance of the templates used to render the cell.
	     */
	    private static Templates templates = GWT.create(Templates.class);

	    @Override
	    public void render(Context context, List<Phone> phones, SafeHtmlBuilder sb) {
	     
	      if (phones == null) {
	        return;
	      }

	      for(Phone phoneNumber:phones){
	      // If the value comes from the user, we escape it to avoid XSS attacks.
	      SafeHtml safeValue = SafeHtmlUtils.fromString(phoneNumber.getNumber()+"("+phoneNumber.getType()+")");

	      // Use the template to create the Cell's html.
	      SafeStyles styles = SafeStylesUtils.fromTrustedString("width: 100%;");
	      SafeHtml rendered = templates.cell(styles, safeValue);
	      sb.append(rendered);
	      }
	    }
	  }
	
	static GreetingServiceAsync service=GWT.create(GreetingService.class);
	
	  public void onModuleLoad() {
		  selectedLink=0;
		  thirdPanel=new HorizontalPanel();
		  success=false;
		  searchLabel=new InlineLabel("Enter some name to search:");
		  searchLabel.setStyleName("searchLabel");
		  searchText=new TextBox();
		  searchText.setStyleName("searchText");
			
		  searchText.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				if(event.getValue().isEmpty()){
					updateTable();
				}
					else{
					service.getContactListByQuery(event.getValue(),0,new AsyncCallback<List<Contact>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(List<Contact> result) {
							 List<Contact> list = dataProvider.getList();
							 list.clear();
							 for(Contact c:result)
								 list.add(c);
						    					
						}
					});
					}
			
				}
			
		});
		  
		  
	       table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		   table.setPageSize(10);
		   
		   updateListSize();			 
		   updateTable();
					
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
	    PhoneCell cl=new PhoneCell();
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
	    List<Contact> list = dataProvider.getList();
	        
	        table.getColumn(1).setSortable(true);
	        ListHandler<Contact> surnameSortHandler = new ListHandler<Contact>(
		            list);
		        surnameSortHandler.setComparator(surnameColumn,
		            new Comparator<Contact>() {
		            @Override
		            public int compare(Contact o1, Contact o2) {
		                if (o1 == o2) {
		                      return 0;
		                    }

		                    // Compare the surname columns.
		                    if (o1 != null) {
		                      return (o2 != null) ? o1.getSurname().compareTo(o2.getSurname()) : 1;
		                    }
		                    return -1;
		            }
		            });
		        table.addColumnSortHandler(surnameSortHandler);
		        table.getColumnSortList().push(surnameColumn);
	        
		        table.getColumn(2).setSortable(true);
		        ListHandler<Contact> nameSortHandler = new ListHandler<Contact>(
		            list);
		        nameSortHandler.setComparator(nameColumn,
		            new Comparator<Contact>() {
		            @Override
		            public int compare(Contact o1, Contact o2) {
		                if (o1 == o2) {
		                      return 0;
		                    }

		                    // Compare the name columns.
		                    if (o1 != null) {
		                      return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
		                    }
		                    return -1;
		            }
		            });
		        table.addColumnSortHandler(nameSortHandler);
		        table.getColumnSortList().push(nameColumn);
		        
		        table.getColumn(0).setSortable(true);
			    ListHandler<Contact> columnSortHandler = new ListHandler<Contact>(
			            list);
			        columnSortHandler.setComparator(idColumn,
			            new Comparator<Contact>() {
			            @Override
			            public int compare(Contact o1, Contact o2) {
			                if (o1 == o2) {
			                      return 0;
			                    }

			                    // Compare the name columns.
			                    if (o1 != null) {
			                      return (o2 != null) ? o1.getId().compareTo(o2.getId()) : 1;
			                    }
			                    return -1;
			            }
			            });
			        table.addColumnSortHandler(columnSortHandler);
			        table.getColumnSortList().push(idColumn);
			       
	    Button add = new Button();
	    add.addClickListener(new ClickListener() {
	        public void onClick(Widget sender) {
	           DialogBox dlg = new ContactDialogBox("add");
	           dlg.center();
	        }
	      });
	    
	    Button edit = new Button();
	    edit.addClickListener(new ClickListener() {
	        public void onClick(Widget sender) {
	           System.out.println(selectedIndex);
	           if(selectedIndex!=null && selectedIndex>0){
	           DialogBox dlg = new ContactDialogBox("edit");
	           dlg.center();
	           }
	           else
	           {
	        	   DialogBox dlg=new ExceptionDialogBox();
	        	   dlg.center();
	           }
	        }
	      });
	      
		 Button remove = new Button();
		    remove.addClickListener( new ClickListener() {
	        public void onClick(Widget sender) {
	        	
	        	 if(selectedIndex!=null && selectedIndex>0){
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
								updateListSize();			 
								updateTable();
					    		table.redraw();
								Window.alert("Selected contact is removed!");
							}
						});
			    		
			    	}
			    	
	        }
	        	 else
		           {
		        	   DialogBox dlg=new ExceptionDialogBox();
		        	   dlg.center();
		           }
	        }
	      });
		    
		    add.setStyleName("addButton");
		    edit.setStyleName("editButton");
		    remove.setStyleName("removeButton");
		    VerticalPanel actionPanel=new VerticalPanel();
		    actionPanel.add(add);
		    actionPanel.add(edit);
		    actionPanel.add(remove);
		     
	    HorizontalPanel firstPanel=new HorizontalPanel();
	    firstPanel.add(searchLabel);
	    firstPanel.add(searchText);
	    
	    HorizontalPanel secondPanel=new HorizontalPanel();
	    secondPanel.add(table);
	    secondPanel.add(actionPanel);
	    
	    table.setWidth("90%");
	    DOM.setStyleAttribute(table.getElement(), "margin","20px" );
	    secondPanel.setWidth("80%");
	    RootPanel.get("root").add(firstPanel);
	    RootPanel.get("root").add(secondPanel);
	    RootPanel.get("root").add(thirdPanel);
	      
	  }
	  public static void updateTable(){
		  if(pageNumber>0){
		  System.out.println("selec "+selectedLink);
		   service.getContactList(selectedLink*10,new AsyncCallback<List<Contact>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(List<Contact> result) {
					if(result.size()==0){
						selectedLink=selectedLink-1;
					    updateTable();
					}
					else{
					 List<Contact> list = dataProvider.getList();
					 list.clear();
					 for(Contact c:result)
						 list.add(c);
				  
					}
				}
			});
		  }
	  }
	  
	  public static void updateListSize(){
		  service.getContactListSize(new AsyncCallback<Long>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Long result) {
					// TODO Auto-generated method stub
					thirdPanel.clear();
					pageNumber=(int) (result/table.getPageSize());
					if(result%table.getPageSize()!=0)
						   pageNumber++;
					System.out.println("PageNumber"+pageNumber);
				    if(pageNumber>0)
				    	selectedLink=1;
				    updateTable();
				    if(pageNumber>1){
					if(pageNumber<=10){
						    for(int i=0;i<pageNumber;i++){
						   
						    	Anchor anchor=new Anchor((i+1)+"");
						    	anchor.setStyleName("linkAnchor");
						    	anchor.addClickListener(new ClickListener() {
									
									@Override
									@Deprecated
									public
									void onClick(Widget sender) {
										// TODO Auto-generated method stub
										Anchor a=(Anchor) sender;
										int pageNum=Integer.parseInt(a.getText());
										selectedLink=pageNum-1;
										System.out.println(pageNum);
										pageNum=(pageNum-1)*10;
										service.getContactList(pageNum, new AsyncCallback<List<Contact>>() {

											@Override
											public void onFailure(Throwable caught) {
												// TODO Auto-generated method stub
												
											}

											@Override
											public void onSuccess(List<Contact> result) {
												// TODO Auto-generated method stub
												
												List<Contact> list = dataProvider.getList();
												list.clear();
												 for(Contact c:result)
													 list.add(c);
											  table.redraw();
												    }
									
											
										});
									}
								});
						   	thirdPanel.add(anchor);
						    //anchor.set
						    }
					}
					else{
						
						for(int i=0;i<10;i++){
							   
					    	Anchor anchor=new Anchor((i+1)+"");
					    	anchor.setStyleName("linkAnchor");
					    	anchor.addClickListener(new ClickListener() {
								
								@Override
								@Deprecated
								public
								void onClick(Widget sender) {
									// TODO Auto-generated method stub
									Anchor a=(Anchor) sender;
									int pageNum=Integer.parseInt(a.getText());
									System.out.println(pageNum);
									selectedLink=pageNum-1;
									pageNum=(pageNum-1)*10;
									service.getContactList(pageNum, new AsyncCallback<List<Contact>>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(List<Contact> result) {
											// TODO Auto-generated method stub
											List<Contact> list = dataProvider.getList();
											list.clear();
											 for(Contact c:result)
												 list.add(c);
										  table.redraw();
											    }
								
										
									});
								}
							});
					   	thirdPanel.add(anchor);
					    //anchor.set
					    }
						Anchor anchor=new Anchor(">>");
				    	anchor.setStyleName("linkAnchor");
				    	anchor.addClickListener(new ClickListener() {
							
							@Override
							@Deprecated
							public
							void onClick(Widget sender) {
								// TODO Auto-generated method stub
								thirdPanel.clear();
								
							}
						});
				    	thirdPanel.add(anchor);
					}
				}
				}
			});
	  }
	  
	  static void addNewContact(){
		  updateListSize();
		  selectedLink=pageNumber;
		  updateTable();
	  }
	  
	  void nextPage(int pageNum){}
	}