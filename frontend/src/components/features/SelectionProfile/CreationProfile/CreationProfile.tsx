import { ImageProfile } from "enums/ImageProfile";
import { useEffect, useLayoutEffect, useState } from "react";
import "./CreationProfile.scss";
import * as profileService from "services/ProfileService";
import AppContext from "components/AppContext/AppContext";
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import Locked from "components/features/Locked/Locked";
import Spinner from "components/shared/spinner/Spinner";

interface TypeProfileProps {
  isMain: boolean;
}

function CreationProfil(props: TypeProfileProps) {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();

  // Form
  const [name, setName] = useState("");
  const [image, setImage] = useState("");
  const [isYoung, setIsYoung] = useState(false);

  const [images, setImages] = useState([""]);

  useEffect(() => {
    if (!context.user.enabled) {
      navigate("/");
    }
    setImage(ImageProfile.P1);
    let _images: string[] = [];
    for (let item in ImageProfile) {
      _images.push(ImageProfile[item as keyof typeof ImageProfile]);
    }
    setImages(_images);
  }, []);

  function handleSubmit(event: any) {
    event.preventDefault();
    const createMainProfile = async () => {
      const fetchedCreation = await profileService.createMainProfile(
        name,
        image
      );
      if (fetchedCreation && fetchedCreation.id) {
        setTimeout(function () {
          window.location.href = "/";
        }, 1000);
      }
    };
    const createProfile = async () => {
      const fetchedCreation = await profileService.createProfile(
        name,
        image,
        isYoung
      );
      if (fetchedCreation && fetchedCreation.id) {
        setTimeout(function () {
          window.location.href = "/";
        }, 1000);
      }
    };

    if (props.isMain) createMainProfile();
    else createProfile();
  }

  function handleChangeName(event: any) {
    setName(event.target.value);
  }

  function getImageFromEnum(imageProfile: string): string {
    let link = "images/profiles/" + imageProfile + ".png";
    return link;
  }

  function setImageForm(value: string) {
    setImage(value);
  }

  function handleIsYoung() {
    setIsYoung(!isYoung);
  }

  return (
    <>
      <div className="creation-profile-container profile-creation-style">
        <form className="form-creation-profil" onSubmit={handleSubmit}>
          <h2 className="title-form">Creation du profil</h2>
          <h4 className="title-style">Nom du profil</h4>
          <input
            className="input-style"
            type="text"
            name="mail"
            value={name}
            onChange={handleChangeName}
          />
          <br/>
          <div className="selection-image">
            {images.map((value, index) => {
              return (
                <div className="profile-image" key={index}>
                  <img
                    src={getImageFromEnum(value)}
                    onClick={() => setImageForm(value)}
                  />
                  {image === value ? (
                    <img
                      className="profile-image-superposition"
                      src="images/verifier.png"
                    />
                  ) : (
                    <></>
                  )}
                </div>
              );
            })}
          </div>
          {props.isMain ? (
            <></>
          ) : (
            <>
              <br/>
              <label>Cocher si profil enfant</label>
              <input type="checkbox" onClick={handleIsYoung} />
            </>
          )}
          <input name="image" value={image} type="hidden" />
          <input className="button-submit" type="submit" value="Créer le profile" />
          {props.isMain ? (
            <></>
          ) : (
            <>
              <Link to="/">Retour</Link>
            </>
          )}
        </form>
      </div>
    </>
  );
}

export default CreationProfil;
