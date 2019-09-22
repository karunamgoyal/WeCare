package kvnb.hostelservicemanagement;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class StepCounter extends AppCompatActivity {
    private static final String TAG = "FitActivity";
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;
    private TextView text;
    // Create Builder View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int MY_CAMERA_REQUEST_CODE = 100;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        text = findViewById(R.id.text);

    }

    @Override
    protected void onStart() {
        super.onStart();
        connectFitness();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                connectFitness();
            }
        }, 0, 100000);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void connectFitness() {
        if (mClient == null){
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ)) // GET STEP VALUES
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                                @Override
                                                public void onConnected(Bundle bundle) {
                                                    Log.e(TAG, "Connected!!!");
                                                    // Now you can make calls to the Fitness APIs.
                                                    findFitnessDataSources();

                                                }

                                                @Override
                                                public void onConnectionSuspended(int i) {
                                                    // If your connection to the sensor gets lost at some point,
                                                    // you'll be able to determine the reason and react to it here.
                                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                                    } else if (i
                                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                                        Log.i(TAG,
                                                                "Connection lost.  Reason: Service Disconnected");
                                                    }
                                                }
                                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.e(TAG, "!_@@ERROR :: Google Play services connection failed. Cause: " + result.toString());
                        }
                    })
                    .build();
        }

    }

    private void findFitnessDataSources() {
        Fitness.SensorsApi.findDataSources(
                mClient,
                new DataSourcesRequest.Builder()
                        .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                        .setDataSourceTypes(DataSource.TYPE_DERIVED)
                        .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.e(TAG, "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.e(TAG, "Data source found: " + dataSource.toString());
                            Log.e(TAG, "Data Source type: " + dataSource.getDataType().getName());
                            //registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA) && mListener == null) {
                                Log.i(TAG, "Data source for TYPE_STEP_COUNT_DELTA found!  Registering.");

                                registerFitnessDataListener(dataSource, DataType.AGGREGATE_STEP_COUNT_DELTA);
                            }
                        }
                    }
                });
    }

    private void registerFitnessDataListener(final DataSource dataSource, DataType dataType) {

        Log.e(TAG,"Function Fired");
        text.setText("Steps");
        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                Log.e(TAG,"Datapoint Fired");
                for (Field field : dataPoint.getDataType().getFields()) {
                    Log.e(TAG,"For Loop Fired");
                    Value val = dataPoint.getValue(field);
                    text.setText(val.toString());
                    Log.e(TAG, "Detected DataPoint field: " + field.getName());
                    Log.e(TAG, "Detected DataPoint value: " + val);

                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(),
                mListener).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.i(TAG, "Listener registered!");
                } else {
                    Log.i(TAG, "Listener not registered.");
                }
            }
        });

    }
}
