import Spinner from "components/shared/spinner/Spinner";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import "./ValidationMail.scss";

import * as userService from "services/UserService";

function ValidationMail() {
  const [loading, setLoading] = useState(true);
  const [searchParams, setSearchParams] = useSearchParams();
  const [validation, setValidation] = useState(false);
  let param = searchParams.get("code");

  useEffect(() => {
    const validationMail = async (localParam: string) => {
      const fetchedValidationMail = await userService.validationMail(
        localParam
      );
      if (fetchedValidationMail) {
        if (
          fetchedValidationMail.message &&
          fetchedValidationMail.message === "OK"
        ) {
          setValidation(true);
        }
      }
    };
    if (param) {
      validationMail(param);
      setLoading(false);
    } else {
      setTimeout(function () {
        window.location.href = "/";
      }, 1000);
    }
  }, []);

  return (
    <>
      {loading ? (
        <Spinner />
      ) : (
        <>
          {validation ? (
            <div>Validation Success (A éditer)</div>
          ) : (
            <div>Validation Failed (A éditer)</div>
          )}
        </>
      )}
    </>
  );
}

export default ValidationMail;
