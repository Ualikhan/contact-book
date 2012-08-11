package my.projects.contactbook.client;

import java.util.*;

import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.Phone;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ContactBook implements EntryPoint,ClickHandler{
	
	InlineLabel searchLabel;
	static TextBox searchText;
	static Long selectedIndex;
	static Integer listSize;
	static List<Contact> contactList=new ArrayList<Contact>();
	static CellTable<Contact> table = new CellTable<Contact>();
    static ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();
    static GreetingServiceAsync service=GWT.create(GreetingService.class);
    Button addButton;
    Button editButton;
    Button removeButton;
    VerticalPanel actionPanel;
    HorizontalPanel firstPanel;
	HorizontalPanel secondPanel;
	static HorizontalPanel thirdPanel;
		
    
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
	
	  public void onModuleLoad() {
		  searchLabel=new InlineLabel("Enter some name to search:");
		  searchLabel.setStyleName("searchLabel");
		  searchText=new TextBox();
		  searchText.setStyleName("searchText");
		  addButton = new Button();
		  editButton = new Button();
		  removeButton = new Button();
		  actionPanel=new VerticalPanel();
		  firstPanel=new HorizontalPanel();
		  secondPanel=new HorizontalPanel();
		  thirdPanel=new HorizontalPanel();
		   
		  searchText.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// TODO Auto-generated method stub
				if(searchText.getValue().isEmpty())
					updateTable();
				else{
					service.getContactListByQuery(searchText.getValue(),0,new AsyncCallback<List<Contact>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(List<Contact> result) {
							
							List<Contact> list=dataProvider.getList();
							list.clear();
							for(Contact c:result)
								list.add(c);
							
							table.setRowCount(result.size());
							table.setRowData(0, result);
						    table.redraw();	
						}
					});
					}
			
				
			}
		});
		  searchText.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				if(event.getValue().isEmpty())
					updateTable();
				else{
					service.getContactListByQuery(event.getValue(),0,new AsyncCallback<List<Contact>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(List<Contact> result) {
							
							List<Contact> list=dataProvider.getList();
							list.clear();
							for(Contact c:result)
								list.add(c);
							
							table.setRowCount(result.size());
							table.setRowData(0, result);
						    table.redraw();	
						}
					});
					}
			
				}
			
		});
		  
		  
	      table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		   //table.setPageSize(10);
		  table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				// TODO Auto-generated method stub
				Range range = table.getVisibleRange();
			    int start = range.getStart();
			    int length = range.getLength();
			    contactList=dataProvider.getList();
			    List<Contact> toSet = new ArrayList<Contact>(length);
			    for (int i = start; i < start + length && i < contactList.size(); i++)
			        toSet.add((Contact) contactList.get(i));
			    table.setRowData(start, toSet);	
			}
		});
		
		 SimplePager.Resources pagerResources =  GWT.create(SimplePager.Resources.class); 
		 SimplePager pager = new SimplePager(TextLocation.CENTER, 
		 pagerResources, false, 0, true); 
		 pager.setDisplay(table); 
		 pager.setPageSize(10); 
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
	    
		TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
	      @Override
	      public String getValue(Contact object) {
	        return object.getName();
	      }
	    };
	    table.addColumn(nameColumn, "Name");

	    PhoneCell cl=new PhoneCell();
	    Column<Contact, List<Phone>> phoneColumn = new Column<Contact, List<Phone>>(cl) {

			@Override
			public List<Phone> getValue(Contact object) {
				// TODO Auto-generated method stub
				return object.getPhones();
			}
		};
	    table.addColumn(phoneColumn, "Phones");

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
	    ListHandler<Contact> surnameSortHandler = new ListHandler<Contact>(list);
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
			       
	    
	    addButton.addClickHandler(this);
	    editButton.addClickHandler(this);
	    removeButton.addClickHandler(this);
		addButton.setStyleName("addButton");
		editButton.setStyleName("editButton");
		removeButton.setStyleName("removeButton");
		actionPanel.add(addButton);
		actionPanel.add(editButton);
		actionPanel.add(removeButton);
		     
	    firstPanel.add(searchLabel);
	    firstPanel.add(searchText);
	   
	    secondPanel.add(table);
	    secondPanel.add(actionPanel);
	    
	    thirdPanel.add(pager);
	    
	    secondPanel.setWidth("80%");
	    table.setWidth("90%");
	    DOM.setStyleAttribute(table.getElement(), "margin","20px" );
	    RootPanel.get("root").add(firstPanel);
	    RootPanel.get("root").add(secondPanel);
	    RootPanel.get("root").add(thirdPanel);
	      
	  }
	  public static void updateTable(){
		   service.getContactList(0,new AsyncCallback<List<Contact>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(List<Contact> result) {
					
						List<Contact> list=dataProvider.getList();
						list.clear();
						for(Contact c:result)
							list.add(c);
						table.setRowCount(result.size());
						table.setRowData(0, result);
					
				}
			});
		  
	  }
	  
	  public static void updateListSize(){
		  service.getContactListSize(new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(Integer result) {
					// TODO Auto-generated method stub
					listSize=(Integer) result;
					table.setRowCount(listSize);
					 updateTable();
				}
			});
	  }
	   
	  static void addNewContact(){
		  searchText.setValue("");
		  updateListSize();
		  updateTable();
	  }
	  	  
	  
	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource()==addButton){
			 DialogBox dlg = new ContactDialogBox("add");
	           dlg.center();
		}
		
		if(event.getSource()==editButton){
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

		if(event.getSource()==removeButton){
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
									searchText.setValue("");
									list.remove(c);
									updateListSize();			 
									updateTable();
						    		table.redraw();
									Window.alert("Selected contact is removed!");
									selectedIndex=(long) 0;
								}
							});
				    		
				    	}
			 }
			 else{
			      DialogBox dlg=new ExceptionDialogBox();
			      dlg.center();
			 }
		}
	}
	
}
	