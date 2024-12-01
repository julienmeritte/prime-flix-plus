import AppContext from "components/AppContext/AppContext";
import { UserRole } from "enums/UserRole";
import React, { useLayoutEffect, useState } from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { setConstantValue } from "typescript";
import Locked from "../Locked/Locked";

import "./PanneauAdmin.scss";
import { User } from "models/user";
import { useCookies } from "react-cookie";
import ManageUser from "./ManageUser/ManageUser";
import ManageVideo from "./ManageVideo/ManageVideo";

enum OngletAdmin {
  MANAGE_USER,
  MANAGE_VIDEO,
}

function PanneauAdmin() {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();

  const [onglet, setOnglet] = useState(OngletAdmin.MANAGE_USER);

  useEffect(() => {
    if (
      !context.user.enabled ||
      context.user.role !== UserRole.ROLE_ADMIN ||
      context.activeProfile.id < 0
    ) {
      navigate("/");
    }
  }, []);

  function handleOngletUser() {
    setOnglet(OngletAdmin.MANAGE_USER);
  }

  function handleOngletVideo() {
    setOnglet(OngletAdmin.MANAGE_VIDEO);
  }

  return (
    <>
      <div className="container-fields-admin">
        <div className="onglets-admin">
          <p onClick={handleOngletUser}>Gestion Utilisateurs</p>
          <p onClick={handleOngletVideo}>Gestion Vidéos</p>
        </div>
        <div className="content-admin">
          {onglet === OngletAdmin.MANAGE_USER && <ManageUser />}
          {onglet === OngletAdmin.MANAGE_VIDEO && <ManageVideo />}
        </div>
      </div>
    </>
  );
}

export default PanneauAdmin;
