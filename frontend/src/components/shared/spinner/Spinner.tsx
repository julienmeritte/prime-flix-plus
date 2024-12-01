import "./Spinner.scss";

import logoLoading from "assets/images/spinner.gif";

function Spinner() {
  return (
    <div className="loading-logo-container">
      <img src={logoLoading} alt="logo-light" className="loading-logo"></img>
    </div>
  );
}

export default Spinner;
