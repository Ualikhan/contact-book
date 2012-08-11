package my.projects.contactbook.client;

import java.util.ArrayList;
import java.util.List;

import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Contact;
import my.projects.contactbook.shared.model.Country;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ChoosePhoneNumberDialogBox extends DialogBox implements ClickHandler{
	
		  HorizontalPanel mainPanel;
		  InlineLabel countryLabel;
		  InlineLabel cityLabel;
		  InlineLabel numberLabel;
		  ListBox countryList;
		  ListBox cityList;
		  TextBox countryCode;
		  TextBox cityCode;
		  TextBox numberText;
		  Label infoLabel;
		  FlexTable table;
		  Button closeButton;
		  HorizontalPanel bottomPanel;
		  ContactDialogBox parent;
		  Object layoutData;
		  int maxScroll;
		  
		  
		  private GreetingServiceAsync service=GWT.create(GreetingService.class);
		  
		  public ChoosePhoneNumberDialogBox(){
			  this.parent=(ContactDialogBox) parent;
			  countryLabel=new InlineLabel("Code of country");
			  cityLabel=new InlineLabel("Code of city");
			  numberLabel=new InlineLabel("Number");
			  mainPanel=new HorizontalPanel();
			  countryList=new ListBox();
			  countryCode=new TextBox();
			  cityCode=new TextBox();
			  numberText=new TextBox();
			  infoLabel=new Label();
			  table=new FlexTable();
			  cityList=new ListBox();
			  maxScroll=0;
			  bottomPanel=new HorizontalPanel();
			  closeButton = new Button();
			  
			  service.listCountry(0,new AsyncCallback<List<Country>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(final List<Country> result) {
					// TODO Auto-generated method stub
					countryList.addItem("Select country code");
					cityList.addItem("Select city code");
					cityList.setEnabled(false);
					for (int i = 0; i < result.size(); i++) {
						  countryList.addItem(result.get(i).getName());
		                
					}
				}
			});
			  
			  countryList.addChangeHandler(new ChangeHandler() {
					
					@Override
					public void onChange(ChangeEvent event) {
						// TODO Auto-generated method stub
						
						Widget sender=(Widget) event.getSource();
						ListBox lb=(ListBox) sender;
						final String countryName=lb.getItemText(lb.getSelectedIndex());
						if(lb.getSelectedIndex()==0){
							countryCode.setValue(null);
							cityList.setSelectedIndex(0);
							cityList.setEnabled(false);
							cityCode.setValue("");
						}
						else{
							
							service.getCountry(countryName,new AsyncCallback<Country>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Country result) {
									// TODO Auto-generated method stub
									countryCode.setValue(result.getCode()+"");
								}
								});
							
							
							cityList.setSelectedIndex(0);
							cityCode.setValue("");
							service.listCity(countryName, new AsyncCallback<List<City>>() {

								@Override
								public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
								}

								@Override
								public void onSuccess(final List<City> result) {
								// TODO Auto-generated method stub
							
								
								cityList.setEnabled(true);
								cityList.clear();
								cityList.addItem("Select city code");
								for(City city:result)
									cityList.addItem(city.getName(),city.getId()+"");
								
								cityList.addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										// TODO Auto-generated method stub
										
										Widget sender=(Widget) event.getSource();
										ListBox cityLb=(ListBox) sender;
										cityCode.setValue("");
										String cityName=cityLb.getValue(cityLb.getSelectedIndex());
										System.out.println("CODE "+cityName);
										System.out.println("NAME "+cityLb.getItemText(cityLb.getSelectedIndex()));
										
										for(City city:result)
											if((city.getId()+"").equals(cityName))
												cityCode.setValue(city.getCode());
															
										int maxLength=11-countryCode.getValue().length()-cityCode.getValue().length();
										String numberInfo="Length: "+maxLength;
										infoLabel.setText(numberInfo);
										numberText.setMaxLength(maxLength);
									}
								});
							}
						});
					}
					}
				});
				  
				 	  
			
			  countryCode.setMaxLength(4);
			  cityCode.setMaxLength(7);
			  table.setWidget(0, 0, countryLabel);
			  table.setWidget(0, 1, cityLabel);
			  table.setWidget(0, 2, numberLabel);
			  table.setWidget(1, 0, countryList);
			  table.setWidget(1, 1, cityList);
			  table.setWidget(1, 2, infoLabel);
			  table.setWidget(2, 0, countryCode);
			  table.setWidget(2, 1, cityCode);
			  table.setWidget(2, 2, numberText);
			  
			  countryLabel.setStyleName("countryLabel");
			  cityLabel.setStyleName("cityLabel");
			  numberLabel.setStyleName("numberLabel");
			  countryList.setStyleName("countryList");
			  cityList.setStyleName("cityList");
			  countryCode.setStyleName("countryCode",true);
			  cityCode.setStyleName("cityCode",true);
			  numberText.setStyleName("numberText",true);
			  closeButton.addClickHandler(this);
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
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			if(event.getSource()==closeButton){
				String phoneNum="";
				if(countryCode.getValue()!=null)
					phoneNum+=countryCode.getValue();
				if(cityCode.getValue()!=null)
					phoneNum+=cityCode.getValue();
				if(numberText.getValue()!=null)
					phoneNum+=numberText.getValue();
				this.parent.setNumberText(layoutData,phoneNum);
				this.parent.setStyle();
				hide();
			
			}
		}
}
