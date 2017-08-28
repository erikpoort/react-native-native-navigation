echo "Finish work on example"
module_name=${PWD##*/}
example_path=example
module_path=$example_path/node_modules/$module_name
echo "Do you want to remove ios/android/src?"
select yn in "Yes" "No"; do
    case $yn in
        Yes ) break;;
        No ) exit;;
    esac
done
rm -rf ./android
rm -rf ./ios
rm -rf ./src
mkdir ./android
mkdir ./ios
mkdir ./src
cp -r $module_path/android ./
cp -r $module_path/ios ./
cp -r $module_path/src ./
echo "Done"