import { useEffect, useState } from "react";
import { Badge, Carousel } from "react-bootstrap";

import { Video } from "models/video";

import logo from "assets/images/affiche.jpg";

import "./VideoCarousel.scss";

import * as videoService from "services/VideoService";
import { Format } from "models/format";
import { useNavigate } from "react-router-dom";

function VideoCarousel(props: any) {
  const [list, setList] = useState(props.listVideo);

  const navigate = useNavigate();

  useEffect(() => {
    console.log("props", props.listVideo);
    //setList([...props.listVideo]);
  }, []);

  useEffect(() => {
    console.log("props", list);
  }, [list]);

  function redirectTo(path: string) {
    let first = path.lastIndexOf("/static/videos/");
    navigate("/watch/" + path.substring(first + 15));
  }

  return (
    <>
      <Carousel interval={null}>
        {props.listVideo.map(
          (value: Video, index: React.Key | null | undefined) => {
            if (value.id >= 0) {
              return (
                <Carousel.Item key={index}>
                  <div className="inner-carou">
                    <img className="carousel-img" src={value.image} />
                    <div className="inner-item-carou">
                      <h3>{value.name}</h3>
                      {value.season >= 0 && (
                        <p className="sous-carou-center">
                          Saison {value.season}
                        </p>
                      )}
                      <br />
                      <div className="sous-carou-description">
                        {value.description}
                      </div>
                      <div className="sous-carou-fin">
                        <p>Créateur: {value.creator}</p>
                        <p>Distribution: {value.distribution}</p>
                        <p>Date de Sortie: {value.date}</p>
                      </div>
                      <div className="place-format">
                        {value.formats.map(
                          (val: Format, inde: React.Key | null | undefined) => (
                            <div key={inde}>
                              {val.quality === "Q240" && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  onClick={() => redirectTo(val.path)}
                                >
                                  240p
                                </Badge>
                              )}
                              {val.quality === "Q360" && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  onClick={() => redirectTo(val.path)}
                                >
                                  360p
                                </Badge>
                              )}
                              {val.quality === "Q480" && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  onClick={() => redirectTo(val.path)}
                                >
                                  480p
                                </Badge>
                              )}
                              {val.quality === "Q720" && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  onClick={() => redirectTo(val.path)}
                                >
                                  720p
                                </Badge>
                              )}
                              {val.quality === "Q1080" && (
                                <Badge
                                  bg="light"
                                  text="dark"
                                  onClick={() => redirectTo(val.path)}
                                >
                                  1080p
                                </Badge>
                              )}
                            </div>
                          )
                        )}
                      </div>
                    </div>
                  </div>
                </Carousel.Item>
              );
            }
          }
        )}
      </Carousel>
    </>
  );
}

export default VideoCarousel;
