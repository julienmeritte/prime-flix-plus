import {
  Routes,
  Route,
  Navigate,
  BrowserRouter,
  useNavigate,
} from "react-router-dom";
import Footer from "./components/core/Footer/Footer";
import Header from "./components/core/Header/Header";
import "bootstrap/dist/css/bootstrap.css";

import "./App.scss";
import Portail from "./components/features/Portail/Portail";

import AppContext from "./components/AppContext/AppContext";
import { useEffect, useState } from "react";
import Home from "components/features/Home/Home";
import { User } from "models/user";
import Connexion from "components/features/Connexion/Connexion";
import * as userService from "services/UserService";
import { useCookies } from "react-cookie";
import Spinner from "components/shared/spinner/Spinner";
import LoaderVideo from "components/features/LoaderVideo/LoaderVideo";
import Register from "components/features/Register/Register";

import axios from "axios";
import ValidationMail from "components/features/ValidationMail/ValidationMail";
import { Profile } from "models/profile";
import CreationProfile from "components/features/SelectionProfile/CreationProfile/CreationProfile";
import PanneauAdmin from "components/features/PanneauAdmin/PanneauAdmin";
import { UserRole } from "enums/UserRole";
import Utilisateur from "components/features/Utilisateur/Utilisateur";

function App() {
  const [user, setUser] = useState(new User());
  const [activeProfile, setActiveProfile] = useState(new Profile());
  const [loading, setLoading] = useState(true);
  const [cookies, setCookie] = useCookies(["token", "refresh", "profil"]);

  const [isRefreshing, setIsRefreshing] = useState(false);

  const userSettings = {
    user: user,
    setUser,
    activeProfile,
    setActiveProfile,
  };

  axios.interceptors.request.use(
    (request) => {
      if (request.headers === undefined) {
        request.headers = {};
      }
      if (cookies.token) {
        request.headers["Authorization"] = "Bearer " + cookies.token;
      }
      request.headers["Content-Type"] = "application/json";
      return request;
    },
    (error) => {
      Promise.reject(error);
    }
  );

  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      if (!isRefreshing) {
        setIsRefreshing(true);
        console.log(error, "--- ERROR INTERCEPTOR ---");
        const status = error.response ? error.response.status : null;

        if (status === 400 || status === 401) {
          console.log("Trying to refresh JWT");
          retry();
        }
        //setIsRefreshing(false);
      }
      return Promise.reject(error);
    }
  );

  async function retry() {
    await fetch("/api/auth/refresh", {
      method: "get",
      headers: new Headers({
        "Content-Type": "Application/json",
        Authorization: "Bearer " + cookies.refresh,
      }),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.token && data.refresh) {
          console.log("Got new JWT");
          let newDate = new Date();
          newDate.setHours(newDate.getHours() + 1);
          setCookie("token", data.token, {
            path: "/",
            expires: newDate,
          });
          setCookie("refresh", data.refresh, {
            path: "/",
            expires: newDate,
          });
        }
      });
  }

  useEffect(() => {
    if (isRefreshing) {
      setIsRefreshing(false);
      setTimeout(function () {
        window.location.reload();
      }, 1000);
    }
    console.log("refresh cookie set");
  }, [cookies.refresh]);

  useEffect(() => {
    const getMe = async () => {
      const fetchedGetMe = await userService.getMe();
      if (fetchedGetMe) {
        console.log("GET ME USER", fetchedGetMe);
        setUser(fetchedGetMe);
        setLoading(false);
      } else {
        setUser(new User());
        setLoading(false);
      }
    };

    getMe();
  }, []);

  useEffect(() => {
    if (cookies.profil && cookies.profil > 0) {
      for (let _profile of user.profiles) {
        if (_profile.id == cookies.profil) {
          setActiveProfile(_profile);
        }
      }
    }
  }, [user]);

  return (
    <AppContext.Provider value={userSettings}>
      <BrowserRouter>
        {!loading ? (
          <>
            <Header />
            <div className="main">
              <Routes>
                {user.id === -1 ? (
                  <Route path="/" element={<Portail />}></Route>
                ) : (
                  <Route path="/" element={<Home />}></Route>
                )}
                <Route path="/watch/:name" element={<LoaderVideo />} />
                <Route path="/admin" element={<PanneauAdmin />} />
                <Route
                  path="/createProfil"
                  element={<CreationProfile isMain={false} />}
                />
                <Route path="/user" element={<Utilisateur />} />
                <Route path="/connexion" element={<Connexion />} />
                <Route path="/register" element={<Register />} />
                <Route path="/validation" element={<ValidationMail />} />
                <Route path="*" element={<Navigate to="/" />} />
              </Routes>
            </div>
            <Footer />
          </>
        ) : (
          <></>
        )}
      </BrowserRouter>
    </AppContext.Provider>
  );
}

export default App;
