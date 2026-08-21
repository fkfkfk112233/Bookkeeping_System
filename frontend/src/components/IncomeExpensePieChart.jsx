import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

import { Pie } from "react-chartjs-2";

ChartJS.register(ArcElement, Tooltip, Legend);

function IncomeExpensePieChart({ data }) {
  const chartData = {
    labels: data.map((item) => item.categoryName),

    datasets: [
      {
        data: data.map((item) => item.amount),
      },
    ],
  };

  return (
    <div
      style={{
        height: "280px",
        position: "relative",
      }}
    >
      <Pie
        data={chartData}
        options={{
          responsive: true,
          maintainAspectRatio: false,
        }}
      />
    </div>
  );
}

export default IncomeExpensePieChart;
