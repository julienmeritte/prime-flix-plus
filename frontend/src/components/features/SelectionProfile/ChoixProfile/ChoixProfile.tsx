import "./ChoixProfile.scss";
import * as profileService from "services/ProfileService";
import { useEffect, useState } from "react";
import React from "react";
import AppContext from "components/AppContext/AppContext";
import { Profile } from "models/profile";
import Spinner from "components/shared/spinner/Spinner";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";

function SelectionProfil() {
  const [loading, setLoading] = useState(false);
  const context = React.useContext(AppContext);
  const [cookies, setCookie] = useCookies(["profil"]);
  const navigate = useNavigate();

  function getImageFromEnum(imageProfile: string): string {
    let link = "images/profiles/" + imageProfile + ".png";
    return link;
  }

  function selectProfile(index: number) {
    setLoading(true);
    for (let profil of context.user.profiles) {
      if (profil.id === index) {
        context.setActiveProfile(profil);
        let newDate = new Date();
        newDate.setHours(newDate.getHours() + 1);
        setCookie("profil", index, {
          path: "/",
          expires: newDate,
        });
      }
    }
    setLoading(false);
  }

  function createProfil() {
    navigate("/createProfil");
  }

  return (
    <>
      {loading ? (
        <Spinner />
      ) : (
        <div className="selection-profil-container">
          {context.user.profiles.map(
            (value: Profile, index: React.Key | null | undefined) => {
              return (
                <div
                  className="user-profile-bloc profile-style"
                  key={index}
                  onClick={() => selectProfile(value.id)}
                >
                  <h4>{value.pseudo}</h4>
                  <div className="profile-image">
                    <img src={getImageFromEnum(value.image)} />
                  </div>
                </div>
              );
            }
          )}
          <div className="user-profile-bloc profile-style" onClick={createProfil}>
            <h4> Créer un profile</h4>
            <div className="">
              <img className="image-plus" src="images/plus.png" />
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default SelectionProfil;
