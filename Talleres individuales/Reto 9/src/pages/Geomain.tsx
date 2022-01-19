import { useState, useEffect } from "react";
import { Wrapper, Status } from "@googlemaps/react-wrapper";
import { Circle, Polyline } from "@react-google-maps/api";
import { Geolocation } from "@awesome-cordova-plugins/geolocation";

import {
  IonContent,
  IonHeader,
  IonPage,
  IonTitle,
  IonToolbar,
  IonRange,
  IonList,
  IonItem,
  IonLabel,
  IonButton,
  IonInput,
} from "@ionic/react";
import { RangeValue } from "@ionic/core";

import Map from "../components/Map";

const customFormatter = (value: number) => `${value} Km`;
const predefinedCoordinates = { latitude: 40.73061, longitude: -73.935242 };

const GeoMain = () => {
  const [coordinates, setCordinates] = useState<any>(predefinedCoordinates);
  const [distance, setDistance] = useState(1);
  const [isAreaVisible, setIsAreaVisible] = useState(true);

  const render = (status: Status) => {
    return <h1>{status}</h1>;
  };

  const geoInstance = Geolocation;

  const getCoordinates = () => {
    geoInstance
      .getCurrentPosition()
      .then((res) => {
        const curLongitude = res.coords.longitude;
        const curLatitude = res.coords.latitude;
        //console.log(long, altit);
        setCordinates({ latitude: curLatitude, longitude: curLongitude });
      })
      .catch((e) => {
        alert(e);
      });
  };

  useEffect(() => {
    getCoordinates();
  }, []);

  const handleClickToMap = () => {
    console.log("clicked map!");
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="tertiary">
          <IonTitle>Mapa</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent fullscreen>
        <div className="w-ful h-full">
          <IonItem color="primary">
            <IonLabel>Indicar radio en Kilometros</IonLabel>
          </IonItem>
          <IonItem color="secondary">
            <IonInput
            type="number"
            min="0"
            max="10"
            value={distance}
            onIonChange={(e) => setDistance(e.detail.value as unknown as number)}
            ></IonInput>
          </IonItem>
          {coordinates !== predefinedCoordinates ? (
            <div>
              {/* <div>{`Latitude: ${coordinates.latitude}`}</div>
              <div>{`Longitude: ${coordinates.longitude}`}</div> */}
            </div>
          ) : (
            <div>Activar GPS</div>
          )}
          <IonButton
            className="absolute mt-4 ml-56 z-50"
            size="small"
            onClick={() => {
              setIsAreaVisible(!isAreaVisible);
            }}
          >
            Toggle Radius
          </IonButton>
          <div className="w-full h-3/4">
            <Wrapper
              apiKey={"AIzaSyCbWIRVnC3OzAyyetPDA1TPdiJb7P3jj5M"}
              render={render}
            >
              <Map
                center={{
                  lat: coordinates.latitude,
                  lng: coordinates.longitude,
                }}
                onClick={handleClickToMap}
                onIdle={() => {
                  console.log("idle");
                }}
                zoom={13}
                style={{ flexGrow: "1", height: "100%" }}
              >
                <Marker
                  position={{
                    lat: coordinates.latitude,
                    lng: coordinates.longitude,
                  }}
                />
                <Circle
                  visible={isAreaVisible}
                  center={{
                    lat: coordinates.latitude,
                    lng: coordinates.longitude,
                  }}
                  options={{
                    strokeWeight: 2,
                    strokeColor: "#426bde",
                    fillOpacity: 0.08,
                    zIndex: -50,
                  }}
                  radius={distance * 1000}
                />

                {/*clicks.map((latLng, i) => (
                <Marker key={i} position={latLng} />
                https://react-google-maps-api-docs.netlify.app/#!/Circle
              ))*/}
              </Map>
            </Wrapper>
          </div>
        </div>
      </IonContent>
    </IonPage>
  );
};

export default GeoMain;
const Marker: React.FC<google.maps.MarkerOptions> = (options) => {
  const [marker, setMarker] = useState<google.maps.Marker>();

  useEffect(() => {
    if (!marker) {
      setMarker(new google.maps.Marker());
    }

    // remove marker from map on unmount
    return () => {
      if (marker) {
        marker.setMap(null);
      }
    };
  }, [marker]);

  useEffect(() => {
    if (marker) {
      marker.setOptions(options);
    }
  }, [marker, options]);

  return null;
};
