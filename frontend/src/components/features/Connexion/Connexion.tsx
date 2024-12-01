import { useEffect, useState } from "react";
import "./Connexion.scss";
import * as userService from "services/UserService";
import AppContext from "components/AppContext/AppContext";
import React from "react";

import logoLoading from "assets/images/spinner.gif";

import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import Spinner from "components/shared/spinner/Spinner";
function Connexion() {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();

  const [hiddenPwd, setHiddenPwd] = useState(true);
  const [pwdType, setPwdType] = useState("password");
  const [failedLogin, setFailedLogin] = useState(false);
  const [loading, setLoading] = useState(false);

  const [mail, setMail] = useState("");
  const [password, setPassword] = useState("");

  const [cookies, setCookie] = useCookies(["token", "refresh"]);

  function hidePassword() {
    if (hiddenPwd === true) {
      setPwdType("text");
    } else {
      setPwdType("password");
    }
    setHiddenPwd(!hiddenPwd);
  }

  function handleSubmit(event: any) {
    event.preventDefault();
    const login = async () => {
      setLoading(true);
      const fetchedLogin = await userService.authenticateUser(mail, password);
      if (fetchedLogin && fetchedLogin.token && fetchedLogin.refresh) {
        setFailedLogin(false);
        let newDate = new Date();
        newDate.setHours(newDate.getHours() + 1);
        setCookie("token", fetchedLogin.token, {
          path: "/",
          expires: newDate,
        });
        setCookie("refresh", fetchedLogin.refresh, {
          path: "/",
          expires: newDate,
        });
        window.location.href = "/";
      } else {
        setLoading(false);
        setFailedLogin(true);
      }
    };

    login();
  }

  function handleChangeMail(event: any) {
    setMail(event.target.value);
  }

  function handleChangePassword(event: any) {
    setPassword(event.target.value);
  }

  /*useEffect(() => {
    if (context.user.id > -1) {
      window.location.href = "/";
    }
  }, [context.user.id]);*/

  return (
    <div className="connexion-container">
      {loading ? (
        <Spinner />
      ) : (
        <div>
          <div className="background">
            <div className="shape"></div>
            <div className="shape"></div>
          </div>
          <div>
            <form className="form-style-conn" onSubmit={handleSubmit}>
              {failedLogin ? (
                <h3 className="h3-style-fail">FAILED TO LOGIN</h3>
              ) : (
                <h3 className="h3-style">Connexion </h3>
              )}
              <label className="label-style">Mail utilisateur</label>
              <input
                className="input-style"
                type="text"
                name="mail"
                value={mail}
                onChange={handleChangeMail}
              />

              <label className="label-style">Password</label>
              <input
                className="input-style"
                type={pwdType}
                name="password"
                value={password}
                onChange={handleChangePassword}
              />
              <div className="mdp-style">
                <label className="label-style">Montrer le mot de passe</label>
                <input
                  className="input-style-checkbox"
                  type="checkbox"
                  onClick={hidePassword}
                />
              </div>

              <input className="button-submit" type="submit" value="connect" />
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Connexion;
