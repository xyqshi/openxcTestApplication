package com.openxc.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openxc.VehicleManager;
import com.openxc.measurements.DistanceUntilChargedDistanceDriven;
import com.openxc.measurements.DistanceUntilChargedRequested;
import com.openxc.measurements.DistanceUntilChargedSetpoint;
import com.openxc.measurements.DistanceUntilChargedSetpointMaximum;
import com.openxc.measurements.DistanceUntilChargedStatus;
import com.openxc.measurements.GreenZoneStatus;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.StateOfCharge;
import com.openxc.measurements.StateOfChargeHoldCommandKwh;
import com.openxc.measurements.StateOfChargeHoldCommandPercent;
import com.openxc.measurements.StateOfChargeHoldKwh;
import com.openxc.measurements.StateOfChargeHoldMaximum;
import com.openxc.measurements.StateOfChargeHoldPercent;
import com.openxc.measurements.StateOfChargeSetpoint;
import com.openxc.measurements.StateOfChargeSetpointCommand;
import com.openxc.measurements.StateOfChargeSetpointMaximum;
import com.openxc.measurements.StateOfChargeSetpointMinimum;
import com.openxc.measurements.TEMCEPUDEnabled;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.remote.VehicleServiceException;

public class openxcTestActivity extends Activity {

    private static String TAG = "VehicleDashboard";

    private VehicleManager mVehicleManager;
    private boolean mIsBound;
    private final Handler mHandler = new Handler();
    
    private TextView StateOfChargeView;
    private TextView StateOfChargeSetpointView;
    private TextView StateOfChargeSetpointMaximumView;    
    private TextView StateOfChargeSetpointMinimumView;
    
    private TextView StateOfChargeHoldPercentView;
    private TextView StateOfChargeHoldMaximumView;
    private TextView StateOfChargeHoldKwhView;
    
    private TextView DistanceUntilChargedSetpointView;
    private TextView DistanceUntilChargedSetpointMaximumView;
    private TextView DistanceUntilChargedDistanceDrivenView;
    
    private TextView GreenZoneStatusView;
    private TextView DistanceUntilChargedStatusView;
    private TextView DistanceUntilChargedRequestedView;
    private TextView TEMCEPUDEnabledView;
    private TextView StateOfChargeHoldCommandPercentView;
    private TextView StateOfChargeHoldCommandKwhView;
    private TextView StateOfChargeSetpointCommandView;
    
    private Button button1;
    private Button button2;
    private Button button3;
    
    private EditText text1;
    private EditText text2;
    private EditText text3;

    StringBuffer mBuffer;

    StateOfCharge.Listener StateOfChargeListener =
            new StateOfCharge.Listener() {
        public void receive(Measurement measurement) {
            final StateOfCharge value =
                (StateOfCharge) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeView.setText(
                			"Normalized SOC: " +
                					value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeSetpoint.Listener StateOfChargeSetpointListener = new StateOfChargeSetpoint.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeSetpoint value = (StateOfChargeSetpoint) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeSetpointView.setText(
                			"Actual SOC Setpoint: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeSetpointMaximum.Listener StateOfChargeSetpointMaximumListener = new StateOfChargeSetpointMaximum.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeSetpointMaximum value = (StateOfChargeSetpointMaximum) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeSetpointMaximumView.setText(
                			"Maximum SOC Setpoint: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeSetpointMinimum.Listener StateOfChargeSetpointMinimumListener = new StateOfChargeSetpointMinimum.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeSetpointMinimum value = (StateOfChargeSetpointMinimum) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeSetpointMinimumView.setText(
                			"Minimum SOC Setpoint: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    
    
    
    StateOfChargeHoldPercent.Listener StateOfChargeHoldPercentListener = new StateOfChargeHoldPercent.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeHoldPercent value = (StateOfChargeHoldPercent) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeHoldPercentView.setText(
                			"Actual SOC Hold (%): " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeHoldMaximum.Listener StateOfChargeHoldMaximumListener = new StateOfChargeHoldMaximum.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeHoldMaximum value = (StateOfChargeHoldMaximum) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeHoldMaximumView.setText(
                			"Maximum SOC Hold (%): " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeHoldKwh.Listener StateOfChargeHoldKwhListener =
            new StateOfChargeHoldKwh.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeHoldKwh value = (StateOfChargeHoldKwh) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeHoldKwhView.setText(
                			"Actual SOC Hold (kWh): " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };
    
    
    

    DistanceUntilChargedSetpoint.Listener DistanceUntilChargedSetpointListener =
            new DistanceUntilChargedSetpoint.Listener() {
    	public void receive(Measurement measurement) {
	    final DistanceUntilChargedSetpoint value = (DistanceUntilChargedSetpoint) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	DistanceUntilChargedSetpointView.setText(
                			"Actual DUC Setpoint: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    DistanceUntilChargedSetpointMaximum.Listener DistanceUntilChargedSetpointMaximumListener =
            new DistanceUntilChargedSetpointMaximum.Listener() {
    	public void receive(Measurement measurement) {
	    final DistanceUntilChargedSetpointMaximum value = (DistanceUntilChargedSetpointMaximum) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	DistanceUntilChargedSetpointMaximumView.setText(
                			"Maximum DUC Setpoint: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };

    DistanceUntilChargedDistanceDriven.Listener DistanceUntilChargedDistanceDrivenListener = new DistanceUntilChargedDistanceDriven.Listener() {
        public void receive(Measurement measurement) {
            final DistanceUntilChargedDistanceDriven value = (DistanceUntilChargedDistanceDriven) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	DistanceUntilChargedDistanceDrivenView.setText(
                			"Current DUC Distance Driven: " + 
                					value.getValue().doubleValue());
                }
            });
        }
    };
    
    
    

    GreenZoneStatus.Listener GreenZoneStatusListener =
            new GreenZoneStatus.Listener() {
        public void receive(Measurement measurement) {
            final GreenZoneStatus value = (GreenZoneStatus) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	GreenZoneStatusView.setText(
                			"GreenZone Mode: " + 
                			value.getValue().booleanValue());
                }
            });
        }
    };

    DistanceUntilChargedStatus.Listener DistanceUntilChargedStatusListener =
            new DistanceUntilChargedStatus.Listener() {
        public void receive(Measurement measurement) {
            final DistanceUntilChargedStatus value =
                (DistanceUntilChargedStatus) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	DistanceUntilChargedStatusView.setText(
                			"DUC Active: " + 
                			value.getValue().booleanValue());
                }
            });
        }
    };


    DistanceUntilChargedRequested.Listener DistanceUntilChargedRequestedListener =
            new DistanceUntilChargedRequested.Listener() {
        public void receive(Measurement measurement) {
            final DistanceUntilChargedRequested value =
                    (DistanceUntilChargedRequested) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	DistanceUntilChargedRequestedView.setText(
                			"Requested DUC Distance: " + 
                			value.getValue().doubleValue());
                }
            });
        }
    };

    TEMCEPUDEnabled.Listener TEMCEPUDEnabledListener =
            new TEMCEPUDEnabled.Listener() {
        public void receive(Measurement measurement) {
            final TEMCEPUDEnabled value = (TEMCEPUDEnabled) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	TEMCEPUDEnabledView.setText(
                			"TEMC EPUD Enabled: " + 
                			value.getValue().booleanValue());
                }
            });
        }
    };

    StateOfChargeHoldCommandPercent.Listener StateOfChargeHoldCommandPercentListener =
            new StateOfChargeHoldCommandPercent.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeHoldCommandPercent value = (StateOfChargeHoldCommandPercent) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeHoldCommandPercentView.setText(
                			"Commanded SOC Hold (%): " + 
                			value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeHoldCommandKwh.Listener StateOfChargeHoldCommandKwhListener =
            new StateOfChargeHoldCommandKwh.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeHoldCommandKwh value = (StateOfChargeHoldCommandKwh) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeHoldCommandKwhView.setText(
                			"Commanded SOC Hold (kWh): " + 
                			value.getValue().doubleValue());
                }
            });
        }
    };

    StateOfChargeSetpointCommand.Listener StateOfChargeSetpointCommandListener =
            new StateOfChargeSetpointCommand.Listener() {
        public void receive(Measurement measurement) {
            final StateOfChargeSetpointCommand value = (StateOfChargeSetpointCommand) measurement;
            mHandler.post(new Runnable() {
                public void run() {
                	StateOfChargeSetpointCommandView.setText(
                			"Commanded SOC Setpoint: " + 
                			value.getValue().doubleValue());
                }
            });
        }
    };

    LocationListener mAndroidLocationListener = new LocationListener() {
        public void onLocationChanged(final Location location) {
            mHandler.post(new Runnable() {
                public void run() {
                    //mAndroidLatitudeView.setText("" +
                        //location.getLatitude());
                    //mAndroidLongitudeView.setText("" +
                        //location.getLongitude());
                }
            });
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");
            mVehicleManager = ((VehicleManager.VehicleBinder)service
                    ).getService();

            try {
            	
                mVehicleManager.addListener(StateOfCharge.class,
                		StateOfChargeListener);
                mVehicleManager.addListener(StateOfChargeSetpoint.class,
                		StateOfChargeSetpointListener);
                mVehicleManager.addListener(StateOfChargeSetpointMaximum.class,
                		StateOfChargeSetpointMaximumListener);
                mVehicleManager.addListener(StateOfChargeSetpointMinimum.class,
                		StateOfChargeSetpointMinimumListener);
                
                
                mVehicleManager.addListener(StateOfChargeHoldPercent.class,
                		StateOfChargeHoldPercentListener);
                mVehicleManager.addListener(StateOfChargeHoldMaximum.class,
                		StateOfChargeHoldMaximumListener);
                mVehicleManager.addListener(StateOfChargeHoldKwh.class,
                		StateOfChargeHoldKwhListener);
                
                
                mVehicleManager.addListener(DistanceUntilChargedSetpoint.class,
                		DistanceUntilChargedSetpointListener);
                mVehicleManager.addListener(DistanceUntilChargedSetpointMaximum.class,
                		DistanceUntilChargedSetpointMaximumListener);
                mVehicleManager.addListener(DistanceUntilChargedDistanceDriven.class,
                		DistanceUntilChargedDistanceDrivenListener);
                
                
                mVehicleManager.addListener(GreenZoneStatus.class,
                		GreenZoneStatusListener);
                mVehicleManager.addListener(DistanceUntilChargedStatus.class,
                		DistanceUntilChargedStatusListener);
                mVehicleManager.addListener(DistanceUntilChargedRequested.class,
                		DistanceUntilChargedRequestedListener);
                mVehicleManager.addListener(TEMCEPUDEnabled.class,
                		TEMCEPUDEnabledListener);
                mVehicleManager.addListener(StateOfChargeHoldCommandPercent.class,
                		StateOfChargeHoldCommandPercentListener);
                mVehicleManager.addListener(StateOfChargeHoldCommandKwh.class,
                		StateOfChargeHoldCommandKwhListener);
                mVehicleManager.addListener(StateOfChargeSetpointCommand.class,
                		StateOfChargeSetpointCommandListener);


            } catch(VehicleServiceException e) {
                Log.w(TAG, "Couldn't add listeners for measurements", e);
            } catch(UnrecognizedMeasurementTypeException e) {
                Log.w(TAG, "Couldn't add listeners for measurements", e);
            }
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleService disconnected unexpectedly");
            mVehicleManager = null;
            mIsBound = false;
        }
    };
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Vehicle dashboard created");
              
        StateOfChargeView = (TextView) findViewById(
                R.id.StateOfCharge);
        StateOfChargeSetpointView = (TextView) findViewById(
                R.id.StateOfChargeSetpoint);
        StateOfChargeSetpointMaximumView = (TextView) findViewById(
                R.id.StateOfChargeSetpointMaximum);
        StateOfChargeSetpointMinimumView = (TextView) findViewById(
                R.id.StateOfChargeSetpointMinimum);
        
        
        
        StateOfChargeHoldPercentView = (TextView) findViewById(
                R.id.StateOfChargeHoldPercent);
        StateOfChargeHoldMaximumView = (TextView) findViewById(
                R.id.StateOfChargeHoldMaximum);
        StateOfChargeHoldKwhView = (TextView) findViewById(
                R.id.StateOfChargeHoldKwh);
        
        
        
        DistanceUntilChargedSetpointView = (TextView) findViewById(
                R.id.DistanceUntilChargedSetpoint);
        DistanceUntilChargedSetpointMaximumView = (TextView) findViewById(
                R.id.DistanceUntilChargedSetpointMaximum);
        DistanceUntilChargedDistanceDrivenView = (TextView) findViewById(
                R.id.DistanceUntilChargedDistanceDriven);
        
        
        
        GreenZoneStatusView = (TextView) findViewById(
                R.id.GreenZoneStatus);
        DistanceUntilChargedStatusView = (TextView) findViewById(
                R.id.DistanceUntilChargedStatus);
        DistanceUntilChargedRequestedView = (TextView) findViewById(
                R.id.DistanceUntilChargedRequested);
        TEMCEPUDEnabledView = (TextView) findViewById(
                R.id.TEMCEPUDEnabled);
        
        
        StateOfChargeHoldCommandPercentView = (TextView) findViewById(
                R.id.StateOfChargeHoldCommandPercent);
        StateOfChargeHoldCommandKwhView = (TextView) findViewById(
                R.id.StateOfChargeHoldCommandKwh);
        StateOfChargeSetpointCommandView = (TextView) findViewById(
                R.id.StateOfChargeSetpointCommand);
        
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
      		@Override
      		public void onClick(View v) {
      			String value1String = text1.getText().toString().trim();
      			double value1 = Double.parseDouble(value1String);
      			StateOfChargeHoldCommandPercent newMeasurement = new StateOfChargeHoldCommandPercent ( (Number) value1);
      			try {
					mVehicleManager.set(newMeasurement);
				} catch (UnrecognizedMeasurementTypeException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
      		}  	
        });
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
      		@Override
      		public void onClick(View v) {
      			String value2String = text2.getText().toString().trim();
      			double value2 = Double.parseDouble(value2String);
      			StateOfChargeHoldCommandKwh newMeasurement = new StateOfChargeHoldCommandKwh ( (Number) value2);
      			try {
					mVehicleManager.set(newMeasurement);
				} catch (UnrecognizedMeasurementTypeException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
      		}  	
        });
        
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
      		@Override
      		public void onClick(View v) {
      			String value3String = text3.getText().toString().trim();
      			double value3 = Double.parseDouble(value3String);
      			StateOfChargeSetpointCommand newMeasurement = new StateOfChargeSetpointCommand ( (Number) value3);
      			try {
					mVehicleManager.set(newMeasurement);
				} catch (UnrecognizedMeasurementTypeException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
      		}  	
        });
        
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        
        mBuffer = new StringBuffer();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindService(new Intent(this, VehicleManager.class),
                mConnection, Context.BIND_AUTO_CREATE);

        LocationManager locationManager = (LocationManager)
            getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(
                    VehicleManager.VEHICLE_LOCATION_PROVIDER, 0, 0,
                    mAndroidLocationListener);
        } catch(IllegalArgumentException e) {
            Log.w(TAG, "Vehicle location provider is unavailable");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mIsBound) {
            Log.i(TAG, "Unbinding from vehicle service");
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}

