import { useEffect, useState } from "react";
import "./Register.scss";
import * as userService from "services/UserService";
import AppContext from "components/AppContext/AppContext";
import React from "react";

import logoLoading from "assets/images/spinner.gif";

import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import Spinner from "components/shared/spinner/Spinner";
function Register() {
  const context = React.useContext(AppContext);
  const navigate = useNavigate();

  const [hiddenPwd, setHiddenPwd] = useState(true);
  const [pwdType, setPwdType] = useState("password");
  const [failedRegister, setFailedRegister] = useState(false);
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
    const register = async () => {
      setLoading(true);
      const fetchedRegister = await userService.register(mail, password);
      if (fetchedRegister) {
        setFailedRegister(false);
        const fetchedLogin = await userService.authenticateUser(mail, password);
          if (fetchedLogin && fetchedLogin.token && fetchedLogin.refresh) {
            setFailedRegister(false);
            setCookie("token", fetchedLogin.token, {
              path: "/",
            });
            setCookie("refresh", fetchedLogin.refresh, {
              path: "/",
            });
            setTimeout(function () {
              window.location.href = "/";
            }, 1000);
          } else {
            setLoading(false);
            setFailedRegister(true);
          }
      }
      
    };

    register();
  }

  function handleChangeMail(event: any) {
    setMail(event.target.value);
  }

  function handleChangePassword(event: any) {
    setPassword(event.target.value);
  }

  useEffect(() => {
    if (context.user.id > -1) {
      navigate("/");
    }
  }, []);

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
                {failedRegister ? <h3 className="h3-style-fail">FAILED REGISTER</h3> : <h3 className="h3-style">Register Here </h3>}
                <label className="label-style">
                  Mail utilisateur
                </label>
                <input
                  className="input-style"
                  type="text"
                  name="mail"
                  value={mail}
                  onChange={handleChangeMail}
                />
              
                <label className="label-style">
                  Password
                </label>
                <input 
                  className="input-style"
                  type={pwdType}
                  name="password"
                  value={password}
                  onChange={handleChangePassword}
                />
                <input className="input-style-checkbox" type="checkbox" onClick={hidePassword} />
              
              <input className="button-submit" type="submit" value="connect" />
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Register;
