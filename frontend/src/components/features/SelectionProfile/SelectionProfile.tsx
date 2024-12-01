import AppContext from "components/AppContext/AppContext";
import Spinner from "components/shared/spinner/Spinner";
import { Profile } from "models/profile";
import React, { useEffect } from "react";
import { useState } from "react";
import ChoixProfile from "./ChoixProfile/ChoixProfile";
import CreationProfile from "./CreationProfile/CreationProfile";
import "./SelectionProfile.scss";

function SelectionProfile() {
  enum ProfileState {
    HAS_MAIN_PROFILE_ONLY,
    HAS_MAIN_PROFILE_AND,
    HAS_NO_MAIN_PROFILE,
    HAS_NO_PROFILE,
    OTHER,
  }

  const context = React.useContext(AppContext);
  const [loading, setLoading] = useState(true);
  const [profileState, setProfileState] = useState(ProfileState.OTHER);

  useEffect(() => {
    if (context.user.profiles) {
      if (context.user.profiles.length > 0) {
        let hasMainProfile = false;
        for (let profile of context.user.profiles) {
          if (profile.isMainProfile) {
            hasMainProfile = true;
          }
        }
        if (hasMainProfile) {
          if (context.user.profiles.length === 1) {
            setProfileState(ProfileState.HAS_MAIN_PROFILE_ONLY);
          } else {
            setProfileState(ProfileState.HAS_MAIN_PROFILE_AND);
          }
        } else setProfileState(ProfileState.HAS_NO_MAIN_PROFILE);
      } else setProfileState(ProfileState.HAS_NO_PROFILE);
      setLoading(false);
    }
  }, []);

  switch (profileState) {
    case ProfileState.HAS_MAIN_PROFILE_AND:
    case ProfileState.HAS_MAIN_PROFILE_ONLY:
      return <ChoixProfile />;
    case ProfileState.HAS_NO_MAIN_PROFILE:
    case ProfileState.HAS_NO_PROFILE:
      return <CreationProfile isMain={true} />;
    default:
      return <Spinner />;
  }
}

export default SelectionProfile;
