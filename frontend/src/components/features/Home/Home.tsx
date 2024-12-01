import AppContext from "components/AppContext/AppContext";
import React from "react";
import { Carousel, Container } from "react-bootstrap";
import { useEffect, useState } from "react";
import SelectionProfile from "../SelectionProfile/SelectionProfile";
import "./Home.scss";
import logo from "assets/images/affiche.jpg";
import VideoCarousel from "../VideoCarousel/VideoCarousel";

import { Video } from "models/video";
import { Format } from "models/format";

import * as videoService from "services/VideoService";

function Home() {
  const context = React.useContext(AppContext);
  const [loading, setLoading] = useState(true);
  const [loaded, setLoaded] = useState(false);

  const [recentSeries, setRecentSeries] = useState([new Video(), new Video()]);
  const [recentAll, setRecentAll] = useState([new Video()]);

  const getAllRecent = async () => {
    const fetchedAllRecent = await videoService.getSortAll();
    if (fetchedAllRecent) {
      setRecentAll([...fetchedAllRecent]);
    }
  };

  const getAllRecentImage = async (index: number, id: number) => {
    const fetchedAllRecent = await videoService.getImage(id);
    if (fetchedAllRecent) {
      console.log("oui", fetchedAllRecent);
      let _list = [...recentAll];
      _list[index].image = fetchedAllRecent;
      setRecentAll(_list);
    }
  };

  useEffect(() => {
    //setRecentSeries([new Video(), _video]);
    getAllRecent();
  }, []);

  useEffect(() => {
    if (recentAll && recentAll[0] && recentAll[0].id >= 0) {
      if (!loaded) {
        for (let i = 0; i < recentAll.length; i++) {
          if (recentAll[i].id >= 0) getAllRecentImage(i, recentAll[i].id);
        }
        setLoaded(true);
      }
    }
  }, [recentAll]);

  return (
    <div className="home-container">
      {context.user.enabled ? (
        <>
          {context.activeProfile.id >= 0 ? (
            <>
              <Container fluid>
                <h2>Sorties récentes</h2>
                <VideoCarousel listVideo={recentAll} />
              </Container>
              <Container fluid>
                <h2>Séries</h2>
                <VideoCarousel listVideo={recentAll} />
              </Container>
              <Container fluid>
                <h2>Films</h2>
                <VideoCarousel listVideo={recentAll} />
              </Container>
            </>
          ) : (
            <SelectionProfile />
          )}
        </>
      ) : (
        <div>
          {" "}
          Veuillez valider votre compte en suivant le lien reçu par email.
        </div>
      )}
    </div>
  );
}

export default Home;
