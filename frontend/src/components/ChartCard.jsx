function ChartCard({ title, children }) {
  return (
    <div className="card shadow-sm h-100">
      <div className="card-body">
        <h5 className="card-title mb-4">{title}</h5>

        {children}
      </div>
    </div>
  );
}

export default ChartCard;
