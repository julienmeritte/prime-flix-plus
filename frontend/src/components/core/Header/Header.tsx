import "./Header.scss";
import logoLight from "assets/images/primeflixplus-light-new.png";
import { useState, MouseEventHandler, useEffect } from "react";
import React from "react";
import AppContext from "components/AppContext/AppContext";
import { Link, useNavigate } from "react-router-dom";

import { useCookies } from "react-cookie";
import { User } from "models/user";
import Spinner from "components/shared/spinner/Spinner";
import { Profile } from "models/profile";

function Header() {
  const [profileExpanded, setProfilExpanded] = useState(false);
  const context = React.useContext(AppContext);

  const [imageProfile, setImageProfile] = useState("none");

  const expandProfilDetail: MouseEventHandler = () => {
    setProfilExpanded(!profileExpanded);
  };

  useEffect(() => {
    if (context.activeProfile && context.activeProfile.image) {
      setImageProfile(
        "url('images/profiles/" + context.activeProfile.image + ".png')"
      );
    }
  }, [context.activeProfile]);

  const navigate = useNavigate();

  const goHome = () => (window.location.href = "/");

  const [cookies, setCookie] = useCookies(["token", "refresh", "profil"]);

  function disconnect() {
    let newDate = new Date();
    newDate.setHours(newDate.getHours() + 1);
    setCookie("token", "", {
      path: "/",
      expires: newDate,
    });
    setCookie("refresh", "", {
      path: "/",
      expires: newDate,
    });
    setCookie("profil", "", {
      path: "/",
      expires: newDate,
    });
    context.setUser(new User());
    context.setActiveProfile(new Profile());
    navigate("/");
  }

  function changeProfile() {
    context.setActiveProfile(new Profile());
    setCookie("profil", "", {
      path: "/",
    });
    navigate("/");
  }

  useEffect(() => {
    if (context.activeProfile.id < 0) setProfilExpanded(false);
  }, [context.activeProfile.id]);

  return (
    <div className="header-container">
      <div className="header-logo-container">
        <img
          src={logoLight}
          alt="logo-light"
          className="header-logo"
          onClick={goHome}
        ></img>
      </div>
      <div className="header-liens">
        {context.user.id > -1 ? (
          <>
            {context.activeProfile.id >= 0 ? (
              <>
                {context.user.role == 3 ? (
                  <Link to="/admin">Admin</Link>
                ) : (
                  <></>
                )}
              </>
            ) : (
              <></>
            )}
          </>
        ) : (
          <></>
        )}
      </div>
      <div className="header-details">
        {context.user.id >= 0 ? (
          <>
            {context.activeProfile.id >= 0 ? (
              <>
                <div
                  className="header-details-images"
                  onClick={expandProfilDetail}
                  style={{
                    backgroundImage: imageProfile,
                  }}
                ></div>
              </>
            ) : (
              <div className="deco-style" onClick={disconnect}>
                Deconnexion
              </div>
            )}
          </>
        ) : (
          <div className="header-details-not-connected">
            <Link to="/register" className="details-sub-link">
              Inscription
            </Link>
            <Link to="/connexion" className="details-sub-link">
              Connexion
            </Link>
          </div>
        )}
      </div>
      {context.activeProfile.id >= 0 && profileExpanded && (
        <div className="header-profile-expanded">
          <p>{context.activeProfile.pseudo}</p>
          <Link to="/user">Modifier le profil</Link>
          <a onClick={changeProfile}>Changer de profil</a>
          <a onClick={disconnect}>Deconnexion</a>
        </div>
      )}
    </div>
  );
}

export default Header;
