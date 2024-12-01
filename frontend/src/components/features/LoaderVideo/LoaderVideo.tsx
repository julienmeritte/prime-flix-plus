import * as videoService from "services/VideoService";
import { useCookies } from "react-cookie";
import { useEffect, useState } from "react";
import axios from "axios";
import React from "react";
import AppContext from "components/AppContext/AppContext";
import { useNavigate, useParams } from "react-router-dom";

import "./LoaderVideo.scss";

function LoaderVideo() {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();
  const [cookies, setCookie] = useCookies(["token", "refresh"]);
  const { name } = useParams();
  const videoPath = "/api/stream/" + name;
  let _image = "/api/stream/image/" + name;

  if (_image.includes("_240.mp4")) {
    let _index = _image.indexOf("_240.mp4");
    _image = _image.substring(0, _index);
  }
  if (_image.includes("_360.mp4")) {
    let _index = _image.indexOf("_360.mp4");
    _image = _image.substring(0, _index);
  }
  if (_image.includes("_480.mp4")) {
    let _index = _image.indexOf("_480.mp4");
    _image = _image.substring(0, _index);
  }
  if (_image.includes("_720.mp4")) {
    let _index = _image.indexOf("_720.mp4");
    _image = _image.substring(0, _index);
  }
  if (_image.includes("_1080.mp4")) {
    let _index = _image.indexOf("_1080.mp4");
    _image = _image.substring(0, _index);
  }
  if (_image.includes("_SOURCE.mp4")) {
    let _index = _image.indexOf("_SOURCE.mp4");
    _image = _image.substring(0, _index);
  }

  useEffect(() => {
    if (!context.user.enabled || context.activeProfile.id < 0) {
      navigate("/");
    }
  }, []);

  return (
    <>
      <div className="loader-container">
        <video
            autoPlay
          src={videoPath}
          width="720px"
          height="480px"
          controls
          preload="none"
          id="video-loader"
          poster={_image}
        ></video>
      </div>
    </>
  );
}

export default LoaderVideo;
