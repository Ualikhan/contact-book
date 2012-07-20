package my.projects.contactbook.client;

import java.lang.ref.PhantomReference;
import java.util.*;

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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
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
	public static int id=0;
	public static int selectedIndex;
	 List<Contact> CONTACTS;
	 CellTable<Contact> table;
	ListDataProvider<Contact> dataProvider;
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
	      SafeHtml safeValue = SafeHtmlUtils.fromString(phone.number+"("+phone.type+")");

	      // Use the template to create the Cell's html.
	      SafeStyles styles = SafeStylesUtils.fromTrustedString("width: 100%;");
	      SafeHtml rendered = templates.cell(styles, safeValue);
	      sb.append(rendered);
	      }
	    }
	  }

	private static class Contact {
		private final int contactId;
	   private String surname;
	    private String name;
	    private List<Phone> phones=null;
	    public Contact() {
	    	  id++;
	    	  contactId=id;
	    	  if(phones==null)
		    	  phones=new ArrayList<Phone>();
		      
		    }
	    public Contact(String name, String surname, String address) {
	    	  id++;
	    	  contactId=id;
		      this.name = name;
		      this.surname = surname;
		      if(phones==null)
		    	  phones=new ArrayList<Phone>();
		      
		    }
	    public Contact(String name, String surname, String address,Phone phone) {
	    	id++;
	    	contactId=id;
	    	this.name = name;
	      this.surname = surname;
	      if(phones==null)
	    	  phones=new ArrayList<Phone>();
	      
	      phones.add(phone);
	    }
	    public void addPhone(Phone phone){
	    	phones.add(phone);
	    }
	    public void setName(String name) {
			this.name = name;
		}
	    public void setSurname(String surname) {
			this.surname = surname;
		}
	  }
	private static class Phone{
	    private final String number;
	    private final String type;
	   public Phone(String number,String type) {
	      this.number=number;
	      this.type=type;
	    }
	  }

	  /**
	   * The list of data to display.
	   */
			static Contact contact1=new Contact("John", "Johnson", "123 Fourth Avenue");
			static Contact contact2=new Contact("Joe", "Doe", "22 Lance Ln");
			static Contact contact3=new Contact("George", "Clooney", "1600 Pennsylvania Avenue",new Phone("+7852278","mobile"));
			
			
	
	  public void onModuleLoad() {
			contact3.addPhone(new Phone("+287429382","home"));
			contact3.addPhone(new Phone("+384593","work"));
			contact3.addPhone(new Phone("+12931802","home"));
			CONTACTS=new ArrayList<Contact>();
		 CONTACTS.add(contact1);
		 CONTACTS.add(contact2);
		 CONTACTS.add(contact3);
		      
	    // Create a CellTable.
	    table = new CellTable<Contact>();
	    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    
	   
	    TextColumn<Contact> idColumn = new TextColumn<Contact>() {
		      @Override
		      public String getValue(Contact object) {
		        return object.contactId+"";
		      }
		    };
		    table.addColumn(idColumn, "Id");
	    
	    TextColumn<Contact> surnameColumn = new TextColumn<Contact>() {
		      @Override
		      public String getValue(Contact object) {
		        return object.surname;
		      }
		    };
		    table.addColumn(surnameColumn, "Surname");
	    // Add a text column to show the name.
	    TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
	      @Override
	      public String getValue(Contact object) {
	        return object.name;
	      }
	    };
	    table.addColumn(nameColumn, "Name");

	    // Add a date column to show the surname.
	    ColorCell cl=new ColorCell();
	    Column<Contact, List<Phone>> dateColumn = new Column<Contact, List<Phone>>(cl) {

			@Override
			public List<Phone> getValue(Contact object) {
				// TODO Auto-generated method stub
				return object.phones;
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
	         selectedIndex=selected.contactId;
	        }
	      }
	    });

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    table.setRowCount(CONTACTS.size(), true);

	    // Push the data into the widget.
	    table.setRowData(0, CONTACTS);
	    
	    dataProvider = new ListDataProvider<Contact>();
	    
	    // Add the cellList to the dataProvider.
	    dataProvider.addDataDisplay(table);
	    
	    List<Contact> list = dataProvider.getList();

        for(int i=0;i<CONTACTS.size();i++)
        list.add(CONTACTS.get(i));
	    // Add it to the root panel.
	    RootPanel.get().add(table);
	    
	    Button add = new Button("Add", new ClickListener() {
	        public void onClick(Widget sender) {
	           DialogBox dlg = new MyDialog("add");
	           dlg.center();
	        }
	      });
	    
	    Button edit = new Button("Edit", new ClickListener() {
	        public void onClick(Widget sender) {
	           DialogBox dlg = new MyDialog("edit");
	           dlg.center();
	        }
	      });

	    Button remove = new Button("Remove", new ClickListener() {
	        public void onClick(Widget sender) {
	        	List<Contact> list = dataProvider.getList();

	        	for(Contact c:list)
			    	if(c.contactId==selectedIndex){
			    		list.remove(c);
			    		table.redraw();
			    	}
	        }
	      });
	      RootPanel.get().add(add);
	      RootPanel.get().add(edit);
	      RootPanel.get().add(remove);
	  }
	  class MyDialog extends DialogBox implements ClickListener {
		  public MyDialog(String action) {
			  if(action.equals("add")){
		    setText("Sample DialogBox");
		    InlineLabel surnameLabel=new InlineLabel("Surname");
		    
		    final TextBox surname=new TextBox();
		    
		    InlineLabel nameLabel=new InlineLabel("Name");
		    
		    final TextBox name=new TextBox();
		    
		    final VerticalPanel phonesPanel=new VerticalPanel();
			 
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
							for(Widget w:phonesPanel){
								if(sender.getParent().equals(w))
									phonesPanel.remove(w);
							}
							
						}
					});
				   
				    deletePhone.setHeight("25%");
				    deletePhone.setWidth("15%");
				    int index=phonesPanel.getWidgetCount();
				    
					final HorizontalPanel hp=new HorizontalPanel();
					    
					hp.insert(l,0);
				    hp.insert(phone, 1);
				    hp.insert(lb, 2);
				    hp.insert(deletePhone, 3);
				    phonesPanel.insert(hp, index);
				}
			});
		    
		    Button okButton = new Button("OK", new ClickListener() {
				
				@Override
				@Deprecated
				public
				void onClick(Widget sender) {
					// TODO Auto-generated method stub
					List<Contact> list = dataProvider.getList();
					c.setName(name.getValue());
					c.setSurname(surname.getValue());
			        
			        // Add the value to the list. The dataProvider will update the cellList.
			        list.add(c);
				    table.redraw();
				    hide();
				    	
				}
			});
		    Button closeButton = new Button("Close", this);
		   
		    VerticalPanel dock = new VerticalPanel();
		    HorizontalPanel hp1=new HorizontalPanel();
		    hp1.add(surnameLabel);
		    hp1.add(surname);
		    
		    dock.add(hp1);

		    HorizontalPanel hp2=new HorizontalPanel();
		    hp2.add(nameLabel);
		    hp2.add(name);
		    
		    dock.add(hp2);
		    		    
		    dock.add(phoneLabel);		    
		    dock.add(phonesPanel);
		    dock.add(addPhoneButton);
		    
		    HorizontalPanel hp3=new HorizontalPanel();
		    hp3.add(okButton);
		    hp3.add(closeButton);
		    
		    dock.add(hp3);
		    
		   dock.setWidth("100%");
		    dock.setHeight("100%");
		    setWidget(dock);
		  }

			  else if(action.equals("edit")){
				    setText("Sample DialogBox");
				    Label surnameLabel=new Label("Surname");
				    
				    final TextBox surname=new TextBox();
				    
				    Label nameLabel=new Label("Name");
				    final TextBox name=new TextBox();
				    List<Contact> list = dataProvider.getList();

				    for(Contact c:list)
				    	if(c.contactId==selectedIndex){
				    surname.setValue(c.surname);
				    name.setValue(c.name);
				    
				    	}
				    
				    final VerticalPanel phonesPanel=new VerticalPanel();
				    for(Contact c:list)
				    	if(c.contactId==selectedIndex){
				    		for(int i=0;i<c.phones.size();i++){
				    		TextBox phone=new TextBox();
				    		phone.setValue(c.phones.get(i).number);
							InlineLabel l=new InlineLabel("Phone");
							ListBox lb=new ListBox();
							lb.addItem("mobile");
						    lb.addItem("home");
						    lb.addItem("work");
						    Button deletePhone=new Button("X");
						    deletePhone.setHeight("25%");
						    deletePhone.setWidth("15%");for(int j=0;j<lb.getItemCount();j++)
						    if(c.phones.get(i).type.equals(lb.getItemText(j)))
						    		lb.setSelectedIndex(j);
							int index=phonesPanel.getWidgetCount();
							HorizontalPanel hp=new HorizontalPanel();
							hp.insert(phone,0);
							hp.insert(l,1);
							hp.insert(lb,2);
							hp.insert(deletePhone, 3);
						    phonesPanel.insert(hp,index);
						    
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
							int index=phonesPanel.getWidgetCount();
							final HorizontalPanel hp=new HorizontalPanel();
							ListBox lb=new ListBox();
							lb.addItem("mobile");
						    lb.addItem("home");
						    lb.addItem("work");
						    Button deletePhone=new Button("X");
						    deletePhone.setHeight("25%");
						    deletePhone.setWidth("15%");
							hp.insert(l,0);
						    hp.insert(phone, 1);
						    hp.insert(lb, 2);
						    hp.insert(deletePhone, 3);
						    phonesPanel.insert(hp, index);
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
						    	if(c.contactId==selectedIndex){
						    c.surname=surname.getValue();
						    c.name=name.getValue();
						    table.redraw();
						    hide();
						    	}
						}
					});
				    okButton.setHeight("30%");
				    Button closeButton = new Button("Close", this);
				    closeButton.setHeight("30%");
				    
				    VerticalPanel dock = new VerticalPanel();
				    HorizontalPanel hp1=new HorizontalPanel();
				    hp1.add(surnameLabel);
				    hp1.add(surname);
				    
				    dock.add(hp1);

				    HorizontalPanel hp2=new HorizontalPanel();
				    hp2.add(nameLabel);
				    hp2.add(name);
				    
				    dock.add(hp2);
				    		    
				    dock.add(phoneLabel);		    
				    dock.add(phonesPanel);
				    dock.add(addPhoneButton);
				    
				    HorizontalPanel hp3=new HorizontalPanel();
				    hp3.add(okButton);
				    hp3.add(closeButton);
				    
				    dock.add(hp3);
				    dock.setWidth("100%");
				    setWidget(dock);
				  }
			  
		  }
		  public void onClick(Widget sender) {
		    hide();
		  }
		}
		
	}