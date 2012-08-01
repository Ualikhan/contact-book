package my.projects.contactbook.client;

import java.util.List;

import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Country;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ChoosePhoneNumberDialogBox extends DialogBox implements ClickListener{
		  HorizontalPanel mainPanel;
		  InlineLabel countryLabel;
		  InlineLabel cityLabel;
		  InlineLabel numberLabel;
		  ListBox countryList;
		  ListBox cityList;
		  IntegerBox countryCode;
		  TextBox cityCode;
		  IntegerBox numberText;
		  FlexTable table;
		  Button okButton;
		  Button closeButton;
		  HorizontalPanel bottomPanel;
		  ContactDialogBox parent;
		  Object layoutData;
		  private GreetingServiceAsync service=GWT.create(GreetingService.class);
		  
		  public ChoosePhoneNumberDialogBox(){
			  this.parent=(ContactDialogBox) parent;
			  countryLabel=new InlineLabel("Code of country");
			  cityLabel=new InlineLabel("Code of city");
			  numberLabel=new InlineLabel("Number");
				  
			  mainPanel=new HorizontalPanel();
			  countryList=new ListBox();
			  countryList.addChangeListener(new ChangeListener() {
				
				@Override
				@Deprecated
				public
				 void onChange(Widget sender) {
					// TODO Auto-generated method stub
					ListBox lb=(ListBox) sender;
					final String countryName=lb.getItemText(lb.getSelectedIndex());
					if(lb.getSelectedIndex()==0){
						countryCode.setValue(null);
						cityList.setSelectedIndex(0);
						cityList.setEnabled(false);
						cityCode.setValue("");
					}
					else{
						cityList.setSelectedIndex(0);
						cityCode.setValue("");
						service.getCountry(countryName, new AsyncCallback<Country>() {

							@Override
							public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
							}

							@Override
							public void onSuccess(final Country result) {
							// TODO Auto-generated method stub
						
							countryCode.setValue(result.getCode());
							cityList.setEnabled(true);
							cityList.clear();
							cityList.addItem("Select city code");
							for(City city:result.getCities())
								cityList.addItem(city.getName());
							
							cityList.addChangeListener(new ChangeListener() {
								
								@Override
								@Deprecated
								public
								void onChange(Widget sender) {
									// TODO Auto-generated method stub
									ListBox cityLb=(ListBox) sender;
									cityCode.setValue("");
									String cityName=cityLb.getItemText(cityLb.getSelectedIndex());
									for(City city:result.getCities())
										if(city.getName().equals(cityName))
											cityCode.setValue(city.getCode());
																		
								}
							});
						}
					});
				}
				}
			});
			    service.listCountry(new AsyncCallback<List<Country>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<Country> result) {
					// TODO Auto-generated method stub
					countryList.addItem("Select country code");
					cityList.addItem("Select city code");
					cityList.setEnabled(false);
					for(Country c:result)
						countryList.addItem(c.getName());			
				}
			});
			  
			  countryCode=new IntegerBox();
			  countryCode.setMaxLength(4);
			  cityCode=new TextBox();
			  cityCode.setWidth("90%");
			  cityCode.setMaxLength(7);
			  numberText=new IntegerBox();
			  table=new FlexTable();
			  cityList=new ListBox();
			  table.setWidget(0, 0, countryLabel);
			  table.setWidget(0, 1, cityLabel);
			  table.setWidget(0, 2, numberLabel);
			  table.setWidget(1, 0, countryList);
			  table.setWidget(1, 1, cityList);
			  table.setWidget(2, 0, countryCode);
			  table.setWidget(2, 1, cityCode);
			  table.setWidget(2, 2, numberText);
			  
			  bottomPanel=new HorizontalPanel();
			  okButton = new Button("OK", new ClickListener() {
				  
					@Override
					@Deprecated
					public
					void onClick(Widget sender) {
						// TODO Auto-generated method stub	
					String phoneNum=countryCode.getValue()+"-"+cityCode.getValue()+"-"+numberText.getValue();
				((ContactDialogBox) parent).setNumberText(layoutData,phoneNum);
				((ContactDialogBox) parent).setStyle();
			  	    hide();
					}	  
				});
			   
			  table.setWidget(3, 1, okButton);
			  closeButton = new Button("<", this);
			 
			  okButton.setStyleName("okButton");
			  closeButton.setStyleName("closeButtonPhone");
			  mainPanel.add(closeButton);
			  mainPanel.add(table);
			  mainPanel.setHeight("100%");

			  setWidget(mainPanel);
			 
		  }

		public void setArgument1(Object layoutData) {
			// TODO Auto-generated constructor stub
			this.layoutData=layoutData;
		}
		public void setArgument2(DialogBox parent) {
			// TODO Auto-generated constructor stub
			
		this.parent=(ContactDialogBox) parent;
		}

		@Override
		@Deprecated
		public
		void onClick(Widget sender) {
			// TODO Auto-generated method stub
		this.parent.setStyle();
		hide();
		}
	
}
